package clib.presentation.event.snackbar.model

import androidx.compose.material3.SnackbarDuration
import kotlin.String

public data class SnackbarEvent(
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration =
        if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    val action: suspend () -> Unit = {},
)



