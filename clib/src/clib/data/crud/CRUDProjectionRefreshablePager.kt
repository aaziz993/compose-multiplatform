package clib.data.crud

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import clib.data.paging.AbstractRefreshablePager
import klib.data.query.BooleanOperand
import klib.data.query.Order
import klib.data.query.Variable
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPagingApi::class)
public class CRUDProjectionRefreshablePager(
    private var projections: List<Variable>,
    private var predicate: BooleanOperand? = null,
    private var orderBy: List<Order> = emptyList(),
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, List<Any?>>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    private val pagingSourceFactory: (properties: List<Variable>, predicate: BooleanOperand?, orderBy: List<Order>) -> PagingSource<Long, List<Any?>>
) : AbstractRefreshablePager<Long, List<Any?>>(
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) {

    override fun createPagingSource(): PagingSource<Long, List<Any?>> = pagingSourceFactory(projections, predicate, orderBy)

    public fun refresh(
        projections: List<Variable>,
        orderBy: List<Order> = emptyList(),
        predicate: BooleanOperand? = null,
    ) {
        this.projections = projections
        this.orderBy = orderBy
        this.predicate = predicate
        refresh()
    }
}
