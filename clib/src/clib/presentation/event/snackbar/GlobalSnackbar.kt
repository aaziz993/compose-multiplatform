package clib.presentation.event.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import clib.data.type.collections.ToLaunchedEffect
import kotlinx.coroutines.launch

/**
 *  Global Snackbar by GlobalSnackbarEventController
 */
@Composable
public fun GlobalSnackbar() {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    GlobalSnackbarEventController.events.ToLaunchedEffect(
        snackbarHostState,
    ) { event ->
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                event.message,
                event.actionLabel,
                event.withDismissAction,
                event.duration,
            )

            if (result == SnackbarResult.ActionPerformed) event.action()
        }
    }
}
