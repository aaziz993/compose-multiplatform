package clib.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import arrow.core.Either
import clib.data.crud.CRUDProjectionRefreshablePager
import clib.data.crud.CRUDRefreshableMutablePager
import clib.data.crud.CRUDRefreshablePager
import clib.data.crud.model.EntityProperty
import clib.data.crud.mutablePager
import clib.data.crud.pager
import clib.data.type.collections.map
import clib.data.type.collections.restartableflow.RestartableMutableStateFlow
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.data.type.collections.restartableflow.restartableStateIn
import klib.data.crud.CoroutineCrudRepository
import klib.data.load.LoadingResult
import klib.data.load.Refresher
import klib.data.load.loading
import klib.data.load.load
import klib.data.load.loadEither
import klib.data.load.loadResult
import klib.data.query.BooleanOperand
import klib.data.query.Order
import klib.data.query.Variable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

// The reasoning 5_000 was chosen for the stopTimeoutMillis can be found in the official Android documentation, which discusses the ANR (Application Not Responding) timeout threshold.
public const val SHARING_STARTED_STOP_TIMEOUT_MILLIS: Long = 5_000

public abstract class ViewModel<T : Any>() : ViewModel() {

    protected open val savedStateHandle: SavedStateHandle = SavedStateHandle()

    protected fun <T> Flow<T>.launchIn(): Job = launchIn(viewModelScope)

    protected fun <T> Flow<T>.stateIn(
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
        initialValue: T,
    ): RestartableStateFlow<T> = restartableStateIn(
        viewModelScope,
        started,
        initialValue,
    )

    protected fun <T, R> StateFlow<T>.map(initialValue: R, transform: suspend (data: T) -> R): StateFlow<R> =
        map(viewModelScope, initialValue, transform)

    protected fun <T> MutableStateFlow<T>.onStartStateIn(
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
        block: suspend (T) -> T,
    ): RestartableMutableStateFlow<T> {
        val stateFlow = onStart { update { value -> block(value) } }.stateIn(started, value)

        return object : RestartableMutableStateFlow<T>, RestartableStateFlow<T> by stateFlow, MutableStateFlow<T> by this {
            override var value: T
                get() = this@onStartStateIn.value
                set(value) {
                    this@onStartStateIn.value = value
                }

            override val replayCache: List<T>
                get() = stateFlow.replayCache

            override suspend fun collect(collector: FlowCollector<T>): Nothing = stateFlow.collect(collector)
        }
    }

    protected fun <T> stateFlow(
        initialValue: LoadingResult<T> = loading(),
        fetcher: suspend (LoadingResult<T>) -> T,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresh: Refresher? = null,
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
    ): RestartableStateFlow<LoadingResult<T>> = load(
        initialValue,
        fetcher,
        observer,
        refresh,
    ).stateIn(started, initialValue)

    protected fun <T> resultStateFlow(
        initialValue: LoadingResult<T> = loading(),
        fetcher: suspend (LoadingResult<T>) -> Result<T>,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresh: Refresher? = null,
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
    ): RestartableStateFlow<LoadingResult<T>> = loadResult(
        initialValue,
        fetcher,
        observer,
        refresh,
    ).stateIn(started, initialValue)

    protected fun <T> eitherStateFlow(
        initialValue: LoadingResult<T> = loading(),
        fetcher: suspend (LoadingResult<T>) -> Either<Throwable, T>,
        observer: (T) -> Flow<T> = { emptyFlow() },
        refresh: Refresher? = null,
        started: SharingStarted = SharingStarted.WhileSubscribed(SHARING_STARTED_STOP_TIMEOUT_MILLIS),
    ): RestartableStateFlow<LoadingResult<T>> = loadEither(
        initialValue,
        fetcher,
        observer,
        refresh,
    ).stateIn(started, initialValue)

    protected fun <T : Any> Flow<PagingData<T>>.cachedIn(): Flow<PagingData<T>> = cachedIn(viewModelScope)

    protected fun <Value : Any> CoroutineCrudRepository<Value>.pager(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        config: PagingConfig = PagingConfig(10),
        initialKey: Long? = null,
        remoteMediator: RemoteMediator<Long, Value>? = null,
        firstItemOffset: Long = 0,
        disablePrepend: Boolean = false,
    ): CRUDRefreshablePager<Value> = pager(
        predicate,
        orderBy,
        config,
        initialKey,
        remoteMediator,
        viewModelScope,
        firstItemOffset,
        disablePrepend,
    )

    protected fun CoroutineCrudRepository<*>.pager(
        properties: List<Variable>,
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        config: PagingConfig = PagingConfig(10),
        initialKey: Long? = null,
        remoteMediator: RemoteMediator<Long, List<Any?>>? = null,
        firstItemOffset: Long = 0,
    ): CRUDProjectionRefreshablePager = pager(
        properties,
        predicate,
        orderBy,
        config,
        initialKey,
        remoteMediator,
        viewModelScope,
        firstItemOffset,
    )

    protected fun <Value : Any> CoroutineCrudRepository<Value>.mutablePager(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        properties: List<EntityProperty>,
        getEntityValues: (Value) -> List<String>,
        createEntity: (Map<String, String>) -> Value,
        config: PagingConfig = PagingConfig(10),
        initialKey: Long? = null,
        remoteMediator: RemoteMediator<Long, Value>? = null,
        firstItemOffset: Long = 0,
        disablePrepend: Boolean = false,
    ): CRUDRefreshableMutablePager<Value> = mutablePager(
        predicate,
        orderBy,
        properties,
        getEntityValues,
        createEntity,
        config,
        initialKey,
        remoteMediator,
        viewModelScope,
        firstItemOffset,
        disablePrepend,
    )

    public abstract fun action(action: T)
}
