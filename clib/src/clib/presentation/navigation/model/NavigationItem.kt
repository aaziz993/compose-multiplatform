package clib.presentation.navigation.model

import clib.presentation.components.model.item.Item
import clib.presentation.components.model.item.SelectableItem

public class NavigationItem(
    public val enabled: Boolean = true,
    public val alwaysShowLabel: Boolean = true,
    item: Item,
    selectedItem: Item = item,
) : SelectableItem(item, selectedItem)
