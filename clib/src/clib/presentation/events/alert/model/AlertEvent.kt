package clib.presentation.events.alert.model

import androidx.compose.runtime.Composable

public data class AlertEvent(
    public val icon: (@Composable () -> Unit)? = null,
    public val title: (@Composable () -> Unit)? = null,
    public val text: (@Composable () -> Unit)? = null,
    public val isError: Boolean = false,
    public val action: (() -> Unit)? = null,
)
