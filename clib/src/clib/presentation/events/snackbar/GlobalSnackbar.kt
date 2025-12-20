package clib.presentation.events.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import clib.data.type.collections.LaunchedEffect
import kotlinx.coroutines.launch

/**
 *  Global Snackbar by GlobalSnackbarEventController.
 */
@Suppress("ComposeModifierMissing")
@Composable
public fun GlobalSnackbar(
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { snackbarData -> Snackbar(snackbarData) },
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarHost(snackbarHostState, modifier, snackbar)

    GlobalSnackbarEventController.events.LaunchedEffect(
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
