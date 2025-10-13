package clib.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

public abstract class AbstractPagingSource<Key : Any, Value : Any>(
    public val disablePrepend: Boolean,
) : PagingSource<Key, Value>() {

    protected abstract suspend fun fetchData(loadKey: Key?, pageSize: Int): List<Value>

    protected abstract fun getPrevKey(loadKey: Key): Key

    protected abstract fun getNextKey(loadKey: Key?): Key?

    @Suppress("UNCHECKED_CAST")
    final override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        val loadKey = params.key

        val pageSize = params.loadSize

        return try {
            val data = fetchData(loadKey, pageSize)

            LoadResult.Page(
                data,
                if (disablePrepend) null else loadKey?.let(::getPrevKey),
                if (data.size < pageSize) null else getNextKey(loadKey),
            )
        }
        catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Key, Value>): Key? = state.anchorPosition?.let { anchorPosition ->
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        state.closestPageToPosition(anchorPosition)?.prevKey?.let(::getNextKey)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.let(::getPrevKey)
    }
}
