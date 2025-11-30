package clib.presentation.navigation.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Routes

@Composable
public fun DeepLink(
    routes: Routes,
    onNavigate: (NavRoute) -> Unit
) {
    // The effect is produced only once, as `Unit` never changes
    DisposableEffect(Unit) {
        // Sets up the listener to call `onNavigate`
        // for the composable that has a matching `navDeepLink` listed
        ExternalUriHandler.listener = { uri ->

        }
        // Removes the listener when the composable is no longer active
        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}
