package clib.presentation.components.dropdown.menu.model

import androidx.compose.runtime.Composable

public data class MenuItem(
    val title: @Composable () -> Unit = {},
    val leadingIcon: @Composable () -> Unit = {},
    val trailingIcon: @Composable () -> Unit = {},
    val enabled: Boolean = true,
    val onAction: () -> Unit,
)
