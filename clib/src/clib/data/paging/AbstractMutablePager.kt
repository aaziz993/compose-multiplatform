package clib.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import klib.data.type.letIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

@OptIn(ExperimentalPagingApi::class)
public abstract class AbstractMutablePager<Key : Any, Value : Any, Mutation : Any>(
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

    protected val mutations: MutableStateFlow<List<Mutation>> = MutableStateFlow(emptyList())

    public val mutatedData: Flow<PagingData<Mutation>> by lazy {
        data.letIf({ remoteMediator == null }) { it.cachedIn(cacheCoroutineScope!!) }
            .combine(mutations, ::mergeMutations)
    }

    protected abstract fun mergeMutations(pagingData: PagingData<Value>, mutations: List<Mutation>): PagingData<Mutation>
}
