package clib.ui.presentation.components.navigation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.ui.presentation.components.model.item.SelectableItem

public class NavigationItem<T : Any>(
    modifier: Modifier = Modifier.Companion,
    selectedModifier: Modifier = modifier,
    public val enabled: Boolean = true,
    public val alwaysShowLabel: Boolean = true,
    text: (@Composable (Modifier) -> Unit)? = null,
    selectedText: (@Composable (Modifier) -> Unit)? = text,
    icon: (@Composable (Modifier) -> Unit)? = null,
    selectedIcon: (@Composable (Modifier) -> Unit)? = icon,
    badge: (@Composable (Modifier) -> Unit)? = null,
    selectedBadge: (@Composable (Modifier) -> Unit)? = badge,
    public val route: T
) : SelectableItem(
    modifier,
    selectedModifier,
    text,
    selectedText,
    icon,
    selectedIcon,
    badge,
    selectedBadge,
)
