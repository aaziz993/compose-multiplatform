package clib.presentation.components.picker.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.model.item.Item

public open class PickerItem<T : Any>(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    badge: @Composable () -> Unit = {},
    public val value: T? = null,
) : Item(modifier, text, icon, badge)
