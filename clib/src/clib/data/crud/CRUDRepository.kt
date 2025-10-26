package clib.data.crud

import clib.data.crud.model.EntityProperty
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import klib.data.crud.CoroutineCrudRepository
import klib.data.query.Order
import klib.data.query.BooleanOperand
import klib.data.query.Variable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList

@OptIn(ExperimentalPagingApi::class)
public fun <Value : Any> CoroutineCrudRepository<Value>.pagingSource(
    predicate: BooleanOperand? = null,
    orderBy: List<Order> = emptyList(),
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): PagingSource<Long, Value> = CRUDPagingSource(
    { limitOffset ->
        find(predicate, orderBy, limitOffset).toList()
    },
    firstItemOffset,
    disablePrepend,
)

@OptIn(ExperimentalPagingApi::class)
public fun <Value : Any> CoroutineCrudRepository<Value>.pager(
    predicate: BooleanOperand? = null,
    orderBy: List<Order> = emptyList(),
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, Value>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): CRUDRefreshablePager<Value> = CRUDRefreshablePager(
    predicate,
    orderBy,
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) { sort, predicate -> pagingSource(sort, predicate, firstItemOffset, disablePrepend) }

@OptIn(ExperimentalPagingApi::class)
public fun <Value : Any> CoroutineCrudRepository<Value>.mutablePager(
    predicate: BooleanOperand? = null,
    orderBy: List<Order> = emptyList(),
    properties: List<EntityProperty>,
    getEntityValues: (Value) -> List<String>,
    createEntity: (Map<String, String>) -> Value,
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, Value>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): CRUDRefreshableMutablePager<Value> = CRUDRefreshableMutablePager(
    predicate,
    orderBy,
    properties,
    getEntityValues,
    createEntity,
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) { sort, predicate -> pagingSource(sort, predicate, firstItemOffset, disablePrepend) }

@OptIn(ExperimentalPagingApi::class)
public fun CoroutineCrudRepository<*>.pagingSource(
    properties: List<Variable>,
    predicate: BooleanOperand? = null,
    orderBy: List<Order> = emptyList(),
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): PagingSource<Long, List<Any?>> = CRUDPagingSource(
    { limitOffset ->
        find(properties, predicate, orderBy, limitOffset).toList()
    },
    firstItemOffset,
    disablePrepend,
)

@OptIn(ExperimentalPagingApi::class)
public fun CoroutineCrudRepository<*>.pager(
    properties: List<Variable>,
    predicate: BooleanOperand? = null,
    orderBy: List<Order> = emptyList(),
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, List<Any?>>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): CRUDProjectionRefreshablePager = CRUDProjectionRefreshablePager(
    properties,
    predicate,
    orderBy,
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) { properties, predicate, orderBy ->
    pagingSource(properties, predicate, orderBy, firstItemOffset, disablePrepend)
}
