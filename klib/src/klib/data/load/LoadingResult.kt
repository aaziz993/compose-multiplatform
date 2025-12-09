package klib.data.load

import arrow.core.Either
import klib.data.load.LoadingResult.Failure
import klib.data.load.LoadingResult.Loading
import klib.data.load.LoadingResult.Success
import klib.data.type.collections.SHARING_STARTED_STOP_TIMEOUT_MILLIS
import klib.data.type.collections.restartableflow.RestartableStateFlow
import klib.data.type.collections.restartableflow.restartableStateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

public sealed interface LoadingResult<T> {

    public val value: T?

    public data class Loading<T>(override val value: T? = null) : LoadingResult<T>

    public data class Success<T>(override val value: T) : LoadingResult<T>

    public data class Failure<T>(val throwable: Throwable, override val value: T? = null) : LoadingResult<T>

    public fun toLoading(): Loading<T> = Loading(value)

    public fun toSuccess(): LoadingResult<T> =
        when (this) {
            is Loading -> value?.let(::Success) ?: Loading()
            is Success -> Success(value)
            is Failure -> value?.let(::Success) ?: Failure(throwable)
        }

    public fun toFailure(throwable: Throwable): Failure<T> = Failure(throwable, value)

    public fun <R> map(block: (T) -> R): LoadingResult<R> = when (this) {
        is Loading -> Loading(value?.let(block))
        is Success -> Success(block(value))
        is Failure -> Failure(throwable, value?.let(block))
    }

    public suspend fun load(block: suspend () -> T): LoadingResult<T> =
        try {
            success(block())
        }
        catch (e: Throwable) {
            toFailure(e)
        }

    public suspend fun loadResult(block: suspend () -> Result<T>): LoadingResult<T> =
        block().fold(
            onSuccess = { value -> success(value) },
            onFailure = { throwable -> toFailure(throwable) },
        )

    public suspend fun loadEither(block: suspend () -> Either<Throwable, T>): LoadingResult<T> =
        block().fold(
            ifRight = { value -> success(value) },
            ifLeft = { throwable -> toFailure(throwable) },
        )

    public fun load(
        fetcher: suspend (LoadingResult<T>) -> T,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresher: Refresher? = null,
    ): Flow<LoadingResult<T>> = load(
        this,
        { result -> result.load { fetcher(result) } },
        observer,
        refresher,
    )

    public fun loadResult(
        fetcher: suspend (LoadingResult<T>) -> Result<T>,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresher: Refresher? = null,
    ): Flow<LoadingResult<T>> = load(
        this,
        { result -> result.loadResult { fetcher(result) } },
        observer,
        refresher,
    )

    public fun loadEither(
        fetcher: suspend (LoadingResult<T>) -> Either<Throwable, T>,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresher: Refresher? = null,
    ): Flow<LoadingResult<T>> = load(
        this,
        { result -> result.loadEither { fetcher(result) } },
        observer,
        refresher,
    )

    public fun loadStateFlow(
        fetcher: suspend (LoadingResult<T>) -> T,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresher: Refresher? = null,
        scope: CoroutineScope,
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
    ): RestartableStateFlow<LoadingResult<T>> = load(
        fetcher,
        observer,
        refresher,
    ).restartableStateIn(scope, started, this)

    public fun loadResultStateFlow(
        fetcher: suspend (LoadingResult<T>) -> Result<T>,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresher: Refresher? = null,
        scope: CoroutineScope,
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
    ): RestartableStateFlow<LoadingResult<T>> = loadResult(
        fetcher,
        observer,
        refresher,
    ).restartableStateIn(scope, started, this)

    public fun loadEitherStateFlow(
        fetcher: suspend (LoadingResult<T>) -> Either<Throwable, T>,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresher: Refresher? = null,
        scope: CoroutineScope,
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
    ): RestartableStateFlow<LoadingResult<T>> = loadEither(
        fetcher,
        observer,
        refresher,
    ).restartableStateIn(scope, started, this)
}

public fun <T> loading(value: T? = null): LoadingResult<T> = Loading(value)

public fun <T> success(value: T): Success<T> = Success(value)

public fun <T> failure(throwable: Throwable, value: T? = null): Failure<T> = Failure(throwable, value)

public fun <T> Result<T>.toLoadingResult(): LoadingResult<T> = fold(
    onSuccess = { value -> success(value) },
    onFailure = { throwable -> failure(throwable) },
)

public fun <T> Either<Throwable, T>.toLoadingResult(): LoadingResult<T> =
    fold(
        ifRight = { value -> success(value) },
        ifLeft = { throwable -> failure(throwable) },
    )

private fun <T> load(
    initialValue: LoadingResult<T>,
    fetcher: suspend (LoadingResult<T>) -> LoadingResult<T>,
    observer: (T) -> Flow<T>,
    refresh: Refresher?,
): Flow<LoadingResult<T>> {
    // Store the latest emitted value in the lastValue.
    var lastValue: LoadingResult<T> = initialValue
    val refresherFlow = refresh?.flow ?: emptyFlow()

    return flow {
        // Emit the initial data. This will make sure we start all the work
        // as soon as this flow is collected.
        emit(initialValue)
        // Every time we collect a refresh flow, we should emit the last
        // value with the Loading.
        refresherFlow.collect {
            // Make sure we do not emit if we are already in a loading state.
            if (lastValue !is Loading) emit(lastValue.toLoading())
        }
    }.flatMapLatest { result -> loader(result, fetcher, observer) }
        // No need to emit similar values, so make them distinct.
        .distinctUntilChanged()
        // Store latest value into lastValue so we can reuse it for the next refresh trigger.
        .onEach { value -> lastValue = value }
}

private fun <T> loader(
    lastResult: LoadingResult<T>,
    fetcher: suspend (LoadingResult<T>) -> LoadingResult<T>,
    observer: (T) -> Flow<T>,
): Flow<LoadingResult<T>> = flow {
    // Little helper method to observe the data and map it to a LoadingResult.
    val observe: (T) -> Flow<LoadingResult<T>> = { value -> observer(value).map(::success) }
    // Whatever happens, emit the current result.
    emit(lastResult)
    when (lastResult) {
        // If the current result is loading, we fetch the data and emit the result.
        is Loading -> {
            val newResult = fetcher(lastResult)
            emit(newResult)
            // If the fetching is successful, we observe the data and emit it.
            if (newResult is Success) emitAll(observe(newResult.value))
        }
        // If the current result is successful, we simply observe and emit the data changes.
        is Success -> emitAll(observe(lastResult.value))

        // Nothing to do in case of failure and not loading.
        else -> Unit
    }
}
