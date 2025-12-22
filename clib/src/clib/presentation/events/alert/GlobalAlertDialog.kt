package clib.presentation.events.alert

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import clib.data.type.collections.LaunchedEffect
import clib.data.type.orErrorColor
import clib.presentation.events.alert.model.AlertEvent

/**
 * Global AlertDialog by GlobalAlertEventController.
 */
@Suppress("ComposeParameterOrder")
@Composable
public fun GlobalAlertDialog(
    confirmButton: @Composable (action: () -> Unit) -> Unit = { action ->
        IconButton(action) {
            Icon(Icons.Default.Check, null)
        }
    },
    modifier: Modifier = Modifier,
    dismissButton: @Composable() ((dismiss: () -> Unit) -> Unit)? = { dismiss ->
        IconButton(dismiss) {
            Icon(Icons.Default.Close, null)
        }
    },
    icon: (@Composable () -> Unit)? = { Icon(Icons.Default.Warning, null) },
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
) {
    var dialogEvent by remember { mutableStateOf<AlertEvent?>(null) }

    GlobalAlertEventController.events.LaunchedEffect { event ->
        dialogEvent = event
    }
    dialogEvent?.let { event ->
        AlertDialog(
            event.dismissRequestAction ?: {},
            event.confirmAction?.let { action -> { confirmButton(action) } } ?: {},
            modifier,
            event.dismissAction?.let { action ->
                dismissButton?.let {
                    { it(action) }
                }
            },
            event.icon ?: icon,
            event.title ?: title,
            event.text ?: text,
            shape,
            containerColor,
            iconContentColor.orErrorColor(event.isError),
            titleContentColor.orErrorColor(event.isError),
            textContentColor.orErrorColor(event.isError),
            tonalElevation,
            properties,
        )
    }
}
