package clib.presentation.components.model.item

public open class SelectableItem(
    private val item: Item,
    private val selectedItem: Item = item,
) {

    public fun item(selected: Boolean = false): Item = if (selected) selectedItem else item
}
