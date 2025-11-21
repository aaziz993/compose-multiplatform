package clib.presentation.components.model.item

public open class SelectableItem(
    public val item: Item,
    public val selectedItem: Item = item,
) {

    public fun item(selected: Boolean): Item = if (selected) selectedItem else item
}
