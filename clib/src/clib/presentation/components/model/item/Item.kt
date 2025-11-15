package clib.presentation.components.model.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public open class Item(
    public val modifier: Modifier = Modifier,
    public val text: @Composable () -> Unit = {},
    public val icon: @Composable () -> Unit = {},
    public val badge: @Composable () -> Unit = {},
)
