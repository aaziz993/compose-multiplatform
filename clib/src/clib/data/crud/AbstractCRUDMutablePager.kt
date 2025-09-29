package clib.data.crud

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.insertFooterItem
import androidx.paging.map
import clib.data.crud.model.EntityProperty
import clib.data.crud.model.EntityItem
import clib.data.paging.AbstractMutablePager
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPagingApi::class)
public abstract class AbstractCRUDMutablePager<Value : Any>(
    public val properties: List<EntityProperty>,
    protected val getEntityValues: (Value) -> List<String>,
    config: PagingConfig,
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, Value>? = null,
    cacheCoroutineScope: CoroutineScope? = null,
) : AbstractMutablePager<Long, Value, EntityItem<Value>>(
    config,
    initialKey,
    remoteMediator,
    cacheCoroutineScope,
) {
    public val idIndex: Int = properties.indexOfFirst(EntityProperty::isId)

    public val idName: String = properties[idIndex].name

    final override fun mergeMutations(pagingData: PagingData<Value>, mutations: List<EntityItem<Value>>): PagingData<EntityItem<Value>> {
        val (insertMutations, mergeMutations) = mutations.partition(EntityItem<Value>::isNew)

        val mergedPagingData = pagingData
            .map(::createItem)
            .map { pagingItem -> mergeMutations.find { it.id == pagingItem.id } ?: pagingItem }

        return insertMutations.fold(mergedPagingData) { acc, v -> acc.insertFooterItem(item = v) }
    }

    private fun createItem(entity: Value): EntityItem<Value> = getEntityValues(entity).let { values ->
        EntityItem(entity, values[idIndex], values)
    }
}
