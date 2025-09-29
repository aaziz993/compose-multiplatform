package clib.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import clib.data.paging.model.RemoteKeys
import kotlin.time.Clock

@OptIn(ExperimentalPagingApi::class)
public abstract class AbstractRemoteMediator<Key : Any, Value : Any>(
    public val cacheTimeout: Int? = null,
) : RemoteMediator<Key, Value>() {

    override suspend fun initialize(): InitializeAction = if (cacheTimeout == null ||
        Clock.System.now().toEpochMilliseconds() - (getCacheCreationTime() ?: 0) < cacheTimeout) {
        InitializeAction.SKIP_INITIAL_REFRESH
    }
    else {
        InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    @Suppress("UNCHECKED_CAST")
    final override suspend fun load(
        loadType: LoadType,
        state: PagingState<Key, Value>
    ): MediatorResult {
        return try {
            // The network load method takes an optional String
            // parameter. For every page after the first, pass the String
            // token returned from the previous page to let it continue
            // from where it left off. For REFRESH, pass null to load the
            // first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    //New Query so clear the DB
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.currentKey
                }

                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    remoteKeys?.prevKey ?: return MediatorResult.Success(remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)

                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with endOfPaginationReached = false because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                    // the end of pagination for append.
                    remoteKeys?.nextKey ?: return MediatorResult.Success(remoteKeys != null)
                }
            }

            val pageSize = state.config.pageSize

            val data = fetchRemoteData(loadKey, pageSize)

            // add custom logic, if you have some API metadata, you can use it as well
            val endOfPaginationReached = if (loadType == LoadType.REFRESH) {
                //New query so we can delete everything.
                refreshCache(data, loadKey, pageSize)
            }
            else {
                cache(data, loadKey, pageSize)
            }

            MediatorResult.Success(endOfPaginationReached)
        }
        catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    public abstract suspend fun fetchRemoteData(loadKey: Key?, pageSize: Int): List<Value>

    public abstract suspend fun refreshCache(items: List<Value>, loadKey: Key?, pageSize: Int): Boolean

    public abstract suspend fun cache(items: List<Value>, loadKey: Key?, pageSize: Int): Boolean

    public abstract suspend fun getRemoteKeys(item: Value): RemoteKeys<Key>?

    public open suspend fun getCacheCreationTime(): Long? = null

    /** LoadType.REFRESH
     * Gets called when it's the first time we're loading data, or when PagingDataAdapter.refresh() is called;
     * so now the point of reference for loading our data is the state.anchorPosition.
     * If this is the first load, then the anchorPosition is null.
     * When PagingDataAdapter.refresh() is called, the anchorPosition is the first visible position in the displayed list, so we will need to load the page that contains that specific item.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Key, Value>): RemoteKeys<Key>? =
    // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        state.anchorPosition?.let(state::closestItemToPosition)?.let { getRemoteKeys(it) }

    /** LoadType.Prepend
     * When we need to load data at the beginning of the currently loaded data set, the load parameter is LoadType.PREPEND
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Key, Value>): RemoteKeys<Key>? =
    // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { getRemoteKeys(it) }

    /** LoadType.Append
     * When we need to load data at the end of the currently loaded data set, the load parameter is LoadType.APPEND
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Key, Value>): RemoteKeys<Key>? =
    // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { getRemoteKeys(it) }
}
