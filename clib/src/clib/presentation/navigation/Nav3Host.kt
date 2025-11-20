package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal that provides access to the parent Router in nested navigation hierarchies.
 *
 * This is used to establish a parent-child relationship between navigation containers.
 * When a nested Nav3Host is created, it can access its parent router through this CompositionLocal
 * to properly handle navigation events like pop() and dropStack().
 *
 * Value is null for root navigation containers (no parent exists).
 * Value is non-null for nested navigation containers (parent router available).
 *
 * Users typically don't need to access this directly - it's managed automatically by Nav3Host.
 */
@Suppress("ComposeCompositionLocalUsage")
internal val LocalParentRouter: ProvidableCompositionLocal<Router?> = compositionLocalOf { null }

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

    val interceptionEnabled = LocalParentRouter.current != null

    CompositionLocalProvider(LocalParentRouter provides router) {
        BackInterceptionProvider(interceptionEnabled, content)
    }
}
