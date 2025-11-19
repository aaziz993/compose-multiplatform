package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect

/**
 * Main composable for setting up Navigation 3 integration.
 *
 * This composable:
 * 1. Connects the router to the navigator for command execution
 * 2. Provides proper lifecycle management (setup/cleanup)
 * 3. Creates a standardized onBack callback for UI components
 *
 * The connection between router and navigator is automatically managed through
 * DisposableEffect, ensuring proper cleanup when the composable leaves the composition.
 *
 * @param router The router instance for issuing navigation commands
 * @param navigator The navigator instance for executing commands (auto-created if not provided)
 * @param content The content composable that receives the navigation setup
 */
@Composable
public fun Nav3Host(
    router: Router,
    navigator: Navigator,
    content: @Composable () -> Unit,
) {
    DisposableEffect(router, navigator) {
        router.navigationActionQueue.setNavigator(navigator)
        onDispose { router.navigationActionQueue.removeNavigator() }
    }

    val interceptionEnabled = LocalRouter.current != null

    CompositionLocalProvider(LocalRouter provides router) {
        BackInterceptionProvider(interceptionEnabled, content)
    }
}
