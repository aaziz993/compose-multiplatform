package clib.data.crud

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import clib.data.paging.AbstractRefreshablePager
import klib.data.query.BooleanOperand
import klib.data.query.Order
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPagingApi::class)
public class CRUDRefreshablePager<Value : Any>(
    private var predicate: BooleanOperand? = null,
    private var orderBy: List<Order> = emptyList(),
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, Value>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    private val pagingSourceFactory: (predicate: BooleanOperand?, orderBy: List<Order>) -> PagingSource<Long, Value>
) : AbstractRefreshablePager<Long, Value>(
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) {

    override fun createPagingSource(): PagingSource<Long, Value> = pagingSourceFactory(predicate, orderBy)

    public fun refresh(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
    ) {
        this.predicate = predicate
        this.orderBy = orderBy
        refresh()
    }
}
