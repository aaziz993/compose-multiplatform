package clib.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPagingApi::class)
public abstract class AbstractRefreshablePager<Key : Any, Value : Any>(
    config: PagingConfig,
    initialKey: Key?,
    remoteMediator: RemoteMediator<Key, Value>?,
    cacheCoroutineScope: CoroutineScope?,
) : AbstractPager<Key, Value>(
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) {

    public final override fun refresh(): Unit = super.refresh()
}
