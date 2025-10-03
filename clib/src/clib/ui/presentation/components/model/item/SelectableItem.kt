package clib.ui.presentation.components.model.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Suppress("ComposeUnstableReceiver")
public open class SelectableItem(
    modifier: Modifier = Modifier,
    public val selectedModifier: Modifier = modifier,
    text: (@Composable (Modifier) -> Unit)? = null,
    public val selectedText: (@Composable (Modifier) -> Unit)? = text,
    icon: (@Composable (Modifier) -> Unit)? = null,
    public val selectedIcon: (@Composable (Modifier) -> Unit)? = icon,
    badge: (@Composable (Modifier) -> Unit)? = null,
    public val selectedBadge: (@Composable (Modifier) -> Unit)? = badge,
) : Item(text, icon, badge, modifier) {

    @Composable
    public fun Text(modifier: Modifier = Modifier, selected: Boolean = false) {
        ((if (selected) selectedText else text) ?: text ?: selectedText)?.invoke(modifier)
    }

    @Composable
    public fun Icon(modifier: Modifier = Modifier, selected: Boolean = false) {
        ((if (selected) selectedIcon else icon) ?: icon ?: selectedIcon)?.invoke(modifier)
    }

    @Composable
    public fun Badge(modifier: Modifier = Modifier, selected: Boolean = false) {
        ((if (selected) selectedBadge else badge) ?: badge ?: selectedBadge)?.invoke(modifier)
    }

    public fun modifier(selected: Boolean = false): Modifier = if (selected) selectedModifier else modifier
}
