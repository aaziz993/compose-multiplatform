package clib.presentation.navigation

import androidx.compose.runtime.Composable
import web.history.history
import web.window.window

@Composable
public actual fun platformOnBack(): () -> Unit =
    if (history.length > 1) history::back else window::close
