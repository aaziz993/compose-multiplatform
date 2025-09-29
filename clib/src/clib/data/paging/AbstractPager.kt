package clib.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import klib.data.type.letIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
public abstract class AbstractPager<Key : Any, Value : Any> (
    public val config: PagingConfig,
    public val initialKey: Key? = null,
    private val remoteMediator: RemoteMediator<Key, Value>?,
    private val cacheCoroutineScope: CoroutineScope?,
) {

    protected lateinit var pagingSource: PagingSource<Key, Value>

    @OptIn(ExperimentalPagingApi::class)
    public val data: Flow<PagingData<Value>> by lazy {
        Pager(config, initialKey, remoteMediator) { createPagingSource().also { pagingSource = it } }
            .flow.letIf({ remoteMediator == null && cacheCoroutineScope != null }) { it.cachedIn(cacheCoroutineScope!!) }
    }

    protected abstract fun createPagingSource(): PagingSource<Key, Value>

    protected open fun refresh(): Unit = pagingSource.invalidate()
}
