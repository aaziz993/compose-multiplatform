package clib.presentation.components.picker.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.model.item.Item

public open class PickerItem<T : Any>(
    text: (@Composable (Modifier) -> Unit)? = null,
    icon: (@Composable (Modifier) -> Unit)? = null,
    badge: (@Composable (Modifier) -> Unit)? = null,
    public val value: T? = null,
) : Item(text = text, icon = icon, badge = badge)
