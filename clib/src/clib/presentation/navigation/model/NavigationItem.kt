package clib.presentation.navigation.model

import clib.presentation.components.model.item.Item
import clib.presentation.components.model.item.SelectableItem

public data class NavigationItem(
    val enabled: Boolean = true,
    val alwaysShowLabel: Boolean = true,
    val item: Item,
    val selectedItem: Item = item,
) : SelectableItem(item, selectedItem)
