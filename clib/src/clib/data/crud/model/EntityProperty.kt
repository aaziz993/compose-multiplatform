package clib.data.crud.model

import clib.ui.presentation.components.textfield.search.model.SearchFieldState
import klib.data.type.BooleanVariable
import klib.data.type.collections.takeIfNotEmpty
import klib.data.Property

public interface EntityProperty : Property {

    public val isId: Boolean
    public fun predicate(state: SearchFieldState): BooleanVariable?
}

public fun List<EntityProperty>.predicate(states: List<SearchFieldState>): BooleanVariable? =
    zip(states).map { (property, state) ->
        if (state.query.isEmpty()) {
            return null
        }

        property.predicate(state)
    }.filterNotNull().takeIfNotEmpty()?.reduce { acc, v -> acc.and(v) }
