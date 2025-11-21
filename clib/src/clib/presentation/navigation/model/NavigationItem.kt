package clib.presentation.navigation.model

import clib.presentation.components.model.item.Item
import clib.presentation.components.model.item.TextSelectableItem

public class NavigationItem(
    public val enabled: Boolean = true,
    public val alwaysShowLabel: Boolean = true,
    item: (text: String) -> Item,
    selectedItem: (text: String) -> Item = item,
) : TextSelectableItem(item, selectedItem)
