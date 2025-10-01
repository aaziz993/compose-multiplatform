package clib.data.crud

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import clib.data.paging.AbstractRefreshablePager
import klib.data.db.crud.model.query.Order
import klib.data.type.BooleanVariable
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPagingApi::class)
public class CRUDRefreshablePager<Value : Any> (
    private var sort: List<Order>? = null,
    private var predicate: BooleanVariable? = null,
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, Value>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
    private val pagingSourceFactory: (sort: List<Order>?, predicate: BooleanVariable?) -> PagingSource<Long, Value>
) : AbstractRefreshablePager<Long, Value>(
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) {

    override fun createPagingSource(): PagingSource<Long, Value> = pagingSourceFactory(sort, predicate)

    public fun refresh(
        sort: List<Order>? = null,
        predicate: BooleanVariable? = null,
    ) {
        this.sort = sort
        this.predicate = predicate
        refresh()
    }
}
