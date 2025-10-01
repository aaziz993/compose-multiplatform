package clib.data.crud

import clib.data.crud.model.EntityProperty
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import klib.data.db.crud.CRUDRepository
import klib.data.db.crud.model.query.Order
import klib.data.BooleanVariable
import klib.data.Variable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList

@OptIn(ExperimentalPagingApi::class)
public fun <Value : Any> CRUDRepository<Value>.pagingSource(
    sort: List<Order>? = null,
    predicate: BooleanVariable? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): PagingSource<Long, Value> = CRUDPagingSource({ find(sort, predicate, it).toList() }, firstItemOffset, disablePrepend)

@OptIn(ExperimentalPagingApi::class)
public fun <Value : Any> CRUDRepository<Value>.pager(
    sort: List<Order>? = null,
    predicate: BooleanVariable? = null,
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, Value>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): CRUDRefreshablePager<Value> = CRUDRefreshablePager(
    sort,
    predicate,
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) { sort, predicate -> pagingSource(sort, predicate, firstItemOffset, disablePrepend) }

@OptIn(ExperimentalPagingApi::class)
public fun <Value : Any> CRUDRepository<Value>.mutablePager(
    sort: List<Order>? = null,
    predicate: BooleanVariable? = null,
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
    sort,
    predicate,
    properties,
    getEntityValues,
    createEntity,
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) { sort, predicate -> pagingSource(sort, predicate, firstItemOffset, disablePrepend) }

@OptIn(ExperimentalPagingApi::class)
public fun CRUDRepository<*>.pagingSource(
    projections: List<Variable>,
    sort: List<Order>? = null,
    predicate: BooleanVariable? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): PagingSource<Long, List<Any?>> = CRUDPagingSource({ find(projections, sort, predicate, it).toList() }, firstItemOffset, disablePrepend)

@OptIn(ExperimentalPagingApi::class)
public fun CRUDRepository<*>.pager(
    projections: List<Variable>,
    sort: List<Order>? = null,
    predicate: BooleanVariable? = null,
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, List<Any?>>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
): CRUDProjectionRefreshablePager = CRUDProjectionRefreshablePager(
    projections,
    sort,
    predicate,
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) { projections, sort, predicate -> pagingSource(projections, sort, predicate, firstItemOffset, disablePrepend) }
