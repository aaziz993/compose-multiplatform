package clib.presentation.event.alert

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import clib.data.type.collections.LaunchedEffect
import clib.presentation.components.dialog.alert.AlertDialog
import clib.presentation.event.alert.model.AlertEvent
import kotlinx.coroutines.launch

/**
 * Global AlertDialog by GlobalAlertEventController
 */
@Composable
public fun GlobalAlertDialog() {
    val coroutineScope = rememberCoroutineScope()
    var alertDialogState by remember { mutableStateOf<AlertEvent?>(null) }

    GlobalAlertEventController.events.LaunchedEffect { event ->
        alertDialogState = event
    }
    alertDialogState?.let { event ->
        AlertDialog(
            message = event.message,
            isError = event.isError,
            onConfirm = event.action,
            onCancel = { coroutineScope.launch { GlobalAlertEventController.sendEvent(null) } },
        )
    }
}
