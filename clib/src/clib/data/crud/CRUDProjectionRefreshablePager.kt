package clib.data.crud

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import clib.data.paging.AbstractRefreshablePager
import klib.data.crud.model.query.Order
import klib.data.BooleanVariable
import klib.data.Variable
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPagingApi::class)
public class CRUDProjectionRefreshablePager(
    private var projections: List<Variable>,
    private var sort: List<Order>? = null,
    private var predicate: BooleanVariable? = null,
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, List<Any?>>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    private val pagingSourceFactory: (projections: List<Variable>, sort: List<Order>?, predicate: BooleanVariable?) -> PagingSource<Long, List<Any?>>
) : AbstractRefreshablePager<Long, List<Any?>>(
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) {

    override fun createPagingSource(): PagingSource<Long, List<Any?>> = pagingSourceFactory(projections, sort, predicate)

    public fun refresh(
        projections: List<Variable>,
        sort: List<Order>? = null,
        predicate: BooleanVariable? = null,
    ) {
        this.projections = projections
        this.sort = sort
        this.predicate = predicate
        refresh()
    }
}
