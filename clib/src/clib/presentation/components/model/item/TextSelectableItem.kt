package clib.presentation.components.model.item

public open class TextSelectableItem(
    public val item: (text: String) -> Item,
    public val selectedItem: (text: String) -> Item = item,
) {

    public fun item(text: String, selected: Boolean = false): Item = if (selected) selectedItem(text) else item(text)
}
