package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

/**
 * Main composable for setting up Navigation 3 integration.
 *
 * This composable:
 * 1. Connects the router to the navigator for action execution
 * 2. Provides proper lifecycle management (setup/cleanup)
 * 3. Creates a standardized onBack callback for UI components
 *
 * The connection between router and navigator is automatically managed through
 * DisposableEffect, ensuring proper cleanup when the composable leaves the composition.
 *
 * @param router The router instance for issuing navigation actions
 * @param navigator The navigator instance for executing actions (auto-created if not provided)
 * @param content The content composable that receives the navigation setup
 */
@Composable
internal fun Nav3Host(
    router: Router,
    navigator: Navigator,
    content: @Composable (
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        router: Router,
    ) -> Unit,
) {
    val parentRouter = LocalRouter.current

    LaunchedEffect(router) {
        check(parentRouter != router) { "Router can't be parent of itself" }
        parentRouter?.next = router
        router.prev = parentRouter
        router.consumeForwarded()
    }

    DisposableEffect(router, navigator) {
        router.navigationActionQueue.setNavigator(navigator)
        onDispose { router.navigationActionQueue.removeNavigator(navigator) }
    }

    val onBack: () -> Unit = remember(router) {
        { router.pop() }
    }

    val interceptionEnabled = parentRouter != null

    CompositionLocalProvider(LocalRouter provides router) {
        BackInterceptionProvider(interceptionEnabled) {
            content(navigator.backStack, onBack, router)
        }
    }
}
