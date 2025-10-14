package clib.presentation.components.lazycolumn.crud.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import clib.data.crud.AbstractCRUDMutablePager
import clib.data.crud.model.EntityProperty
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.crud.CRUDRepository
import klib.data.crud.model.query.Order
import klib.data.BooleanVariable
import kotlin.Any
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.let
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
public class CRUDViewModel<T : Any>(
    private val repository: CRUDRepository<T>,
    sort: List<Order>? = null,
    predicate: BooleanVariable? = null,
    properties: List<EntityProperty>,
    getEntityValues: (T) -> List<String>,
    createEntity: (Map<String, String>) -> T,
    config: PagingConfig = PagingConfig(10),
    initialKey: Long? = null,
    remoteMediator: RemoteMediator<Long, T>? = null,
    firstItemOffset: Long = 0,
    override val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : AbstractViewModel<CRUDAction<T>>() {

    public val pager: AbstractCRUDMutablePager<T>
        field = repository.mutablePager(
            sort,
            predicate,
            properties,
            getEntityValues,
            createEntity,
            config,
            initialKey,
            remoteMediator,
            firstItemOffset,
        )

    override fun action(action: CRUDAction<T>) {
        when (action) {
            is CRUDAction.Find -> {
//                pager.refresh(action.sort, pager.properties.predicate(action.searchFieldStates))
            }

            CRUDAction.New -> pager.new()

            is CRUDAction.NewFrom<T> -> pager.newFrom(action.item)

            is CRUDAction.ToggleEdit -> pager.editOrUnEdit(action.item)

            is CRUDAction.SelectOrUnselect -> pager.selectOrUnselect(action.item)

            is CRUDAction.SetValue -> pager.setValue(action.id, action.index, action.value)

            is CRUDAction.SelectAll -> pager.selectAll(action.items)

            is CRUDAction.Remove -> pager.remove(action.id)

            CRUDAction.UnselectAll -> pager.unselectAll()

            CRUDAction.NewFromSelected -> pager.newFromSelected()

            CRUDAction.RemoveSelected -> pager.removeSelectedNews()

            CRUDAction.EditSelected -> pager.editSelected()

            CRUDAction.SaveSelected -> viewModelScope.launch {
                repository.insert(pager.selectedNewEntities)
                repository.update(pager.selectedNewEntities)
            }

            CRUDAction.DeleteSelected -> viewModelScope.launch {
                pager.selectedIdPredicate?.let { predicate -> repository.delete(predicate) }
            }

            is CRUDAction.Save -> viewModelScope.launch {
                val entity = pager.createEntity(action.item)
                if (action.item.isNew) repository.insert(entity) else repository.update(entity)
            }

            is CRUDAction.Delete -> viewModelScope.launch {
                repository.delete(pager.idPredicate(action.id))
            }

            CRUDAction.Refresh -> pager.refresh()
        }
    }
}
