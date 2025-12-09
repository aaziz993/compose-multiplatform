package klib.data.load

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either
import klib.data.load.LoadingResult.Failure
import klib.data.load.LoadingResult.Idle
import klib.data.load.LoadingResult.Loading
import klib.data.load.LoadingResult.Success
import kotlin.also
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

public sealed interface LoadingResult<T> {

    public data class Idle<T>(val throwable: Throwable? = null) : LoadingResult<T>

    public data class Loading<T>(val value: T) : LoadingResult<T>

    public data class Success<T>(val value: T) : LoadingResult<T>

    public data class Failure<T>(val value: T, val throwable: Throwable) : LoadingResult<T>

    public fun toLoading(): LoadingResult<T> =
        when (this) {
            is Idle -> Idle()
            is Loading -> Loading(value)
            is Success -> Loading(value)
            is Failure -> Loading(value)
        }

    public fun toSuccess(throwable: Throwable): LoadingResult<T> =
        when (this) {
            is Idle -> Idle(throwable)
            is Loading -> Success(value)
            is Success -> Success(value)
            is Failure -> Success(value)
        }

    public fun toFailure(throwable: Throwable): LoadingResult<T> =
        when (this) {
            is Idle -> Idle(throwable)
            is Loading -> Failure(value, throwable)
            is Success -> Failure(value, throwable)
            is Failure -> Failure(value, throwable)
        }
}

public fun <T> idle(throwable: Throwable? = null): LoadingResult<T> = Idle(throwable)

public fun <T> loading(value: T): Loading<T> = Loading(value)

public fun <T> success(value: T): Success<T> = Success(value)

public fun <T> failure(value: T, throwable: Throwable): Failure<T> = Failure(value, throwable)

public inline fun <T, R> LoadingResult<T>.map(block: (T) -> R): LoadingResult<R> = when (this) {
    is Idle -> Idle(throwable)
    is Loading -> Loading(block(value))
    is Success -> Success(block(value))
    is Failure -> Failure(block(value), throwable)
}

public inline fun <T> LoadingResult<T>.onIdle(
    block: (Throwable?) -> Unit = {},
    other: (LoadingResult<T>) -> Unit = {},
): LoadingResult<T> = also {
    if (it is Idle) block(it.throwable) else other(it)
}

public inline fun <T> LoadingResult<T>.onLoading(
    other: (LoadingResult<T>) -> Unit = {},
    block: (T) -> Unit = {},
): LoadingResult<T> = also { result ->
    if (result is Loading) block(result.value) else other(result)
}

public inline fun <T> LoadingResult<T>.onSuccess(
    other: (LoadingResult<T>) -> Unit = {},
    block: (T) -> Unit = {},
): LoadingResult<T> = also { result ->
    if (result is Success) block(result.value) else other(result)
}

public inline fun <T> LoadingResult<T>.onFailure(
    other: (LoadingResult<T>) -> Unit = { },
    block: (data: T, throwable: Throwable) -> Unit = { _, _ -> },
): LoadingResult<T> = also { result ->
    if (result is Failure) block(result.value, result.throwable) else other(result)
}

public fun <T> Result<T>.toLoadingResult(): LoadingResult<T> = fold(
    onSuccess = { value -> success(value) },
    onFailure = { throwable -> idle(throwable) },
)

public fun <T> Either<Throwable, T>.toLoadingResult(): LoadingResult<T> =
    fold(
        ifRight = { value -> success(value) },
        ifLeft = { throwable -> idle(throwable) },
    )

public inline fun <T> load(
    initialValue: LoadingResult<T> = idle(),
    crossinline fetcher: suspend (LoadingResult<T>) -> T,
    crossinline observer: (T) -> Flow<T> = { emptyFlow() },
    refresh: Refresher? = null,
): Flow<LoadingResult<T>> = loadResult(
    initialValue,
    { result -> runCatching { fetcher(result) } },
    observer,
    refresh,
)

public inline fun <T> loadResult(
    initialValue: LoadingResult<T> = idle(),
    crossinline fetcher: suspend (LoadingResult<T>) -> Result<T>,
    crossinline observer: (T) -> Flow<T> = { emptyFlow() },
    refresh: Refresher? = null,
): Flow<LoadingResult<T>> = loadHelper(
    initialValue,
    { result -> fetcher(result).toLoadingResult() },
    observer,
    refresh,
)

public inline fun <T> loadEither(
    initialValue: LoadingResult<T> = idle(),
    crossinline fetcher: suspend (LoadingResult<T>) -> Either<Throwable, T>,
    crossinline observer: (T) -> Flow<T> = { emptyFlow() },
    refresh: Refresher? = null,
): Flow<LoadingResult<T>> = loadHelper(
    initialValue,
    { result -> fetcher(result).toLoadingResult() },
    observer,
    refresh,
)

@PublishedApi
internal inline fun <T> loadHelper(
    initialValue: LoadingResult<T>,
    crossinline fetcher: suspend (LoadingResult<T>) -> LoadingResult<T>,
    crossinline observer: (T) -> Flow<T>,
    refresh: Refresher?,
): Flow<LoadingResult<T>> {
    // Store the latest emitted value in the lastValue.
    var lastValue = initialValue

    val refresherFlow = refresh?.flow ?: emptyFlow()
    return flow {
        // Emit the initial data. This will make sure we start all the work.
        // as soon as this flow is collected.
        emit(lastValue)
        // Every time we collect a refresh event, we should emit the last.
        // value with the isLoading flag turned to true.
        refresherFlow.collect {
            // Make sure we do not emit if we are already in a loading state.
            if (lastValue !is Idle && lastValue !is Loading) emit(lastValue.toLoading())
        }
    }.flatMapLatest { result -> result.loader(fetcher, observer) }
        // No need to emit similar values, so make them distinct.
        .distinctUntilChanged()
        // Store latest value into lastValue so we can reuse it for the next refresh trigger.
        .onEach { value -> lastValue = value }
}

@PublishedApi
internal inline fun <T> LoadingResult<T>.loader(
    crossinline fetcher: suspend (LoadingResult<T>) -> LoadingResult<T>,
    crossinline observer: (T) -> Flow<T>,
): Flow<LoadingResult<T>> {
    val result = this
    return flow {
        // Little helper method to observe the data and map it to a LoadingResult.
        val observe: (T) -> Flow<LoadingResult<T>> = { value -> observer(value).map(::success) }
        // Whatever happens, emit the current result.
        emit(result)
        when (result) {
            // If the current result is loading, we fetch the data and emit the result.
            is Idle, is Loading<*> -> {
                val newResult = fetcher(result)
                emit(newResult)
                // If the fetching is successful, we observe the data and emit it.
                newResult.onSuccess { value -> emitAll(observe(value)) }
            }

            // If the current result is successful, we simply observe and emit the data changes.
            is Success -> emitAll(observe(result.value))

            // Nothing to do in case of failure and not loading.
            else -> Unit
        }
    }
}
