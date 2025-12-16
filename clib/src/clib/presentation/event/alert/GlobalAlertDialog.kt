package clib.presentation.event.alert

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import clib.data.type.collections.LaunchedEffect
import clib.presentation.event.alert.model.AlertEvent
import kotlinx.coroutines.launch

/**
 * Global AlertDialog by GlobalAlertEventController.
 */
@Composable
public fun GlobalAlertDialog(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = { Icon(Icons.Default.CrisisAlert, null) },
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    textContentErrorColor: Color = MaterialTheme.colorScheme.error,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
    actionButton: @Composable (action: () -> Unit) -> Unit = { action ->
        IconButton(action) {
            Icon(Icons.Default.Check, null)
        }
    },
) {
    val coroutineScope = rememberCoroutineScope()
    var dialogEvent by remember { mutableStateOf<AlertEvent?>(null) }

    GlobalAlertEventController.events.LaunchedEffect { event ->
        dialogEvent = event
    }
    dialogEvent?.let { event ->
        AlertDialog(
            onDismissRequest = { coroutineScope.launch { GlobalAlertEventController.sendEvent(null) } },
            confirmButton = event.action?.let { action -> { actionButton(action) } } ?: {},
            modifier = modifier,
            icon = { Icon(Icons.Default.CrisisAlert, "") },
            title = title,
            text = { Text(event.message) },
            shape = shape,
            containerColor = containerColor,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = if (event.isError) textContentErrorColor else textContentColor,
            tonalElevation = tonalElevation,
            properties = properties,
        )
    }
}
