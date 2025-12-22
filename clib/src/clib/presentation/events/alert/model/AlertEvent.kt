package clib.presentation.events.alert.model

import androidx.compose.runtime.Composable
import clib.presentation.events.alert.GlobalAlertEventController

public data class AlertEvent(
    public val dismissRequestAction: (() -> Unit)? = {
        GlobalAlertEventController.sendEvent(null)
    },
    public val confirmAction: (() -> Unit)? = null,
    public val dismissAction: (() -> Unit)? = {
        GlobalAlertEventController.sendEvent(null)
    },
    public val icon: (@Composable () -> Unit)? = null,
    public val title: (@Composable () -> Unit)? = null,
    public val text: (@Composable () -> Unit)? = null,
    public val isError: Boolean = false,
)
