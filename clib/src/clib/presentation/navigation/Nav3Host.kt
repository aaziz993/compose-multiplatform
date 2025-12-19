package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import clib.data.net.GlobalDeepLinkEvents
import clib.presentation.event.EventBus
import clib.presentation.navigation.result.LocalResultEventBus
import clib.presentation.navigation.result.LocalResultStore
import clib.presentation.state.rememberStateStore
import io.ktor.http.Url

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
    onDeepLink: Router.(Url) -> Unit,
    content: @Composable (
        router: Router,
        backStack: List<NavRoute>,
        onBack: () -> Unit,
    ) -> Unit,
) {
    // Return a result from one screen to a previous screen using a state-based approach.
    val resultStore = rememberStateStore()
    // Return a result from one screen to a previous screen using an event-based approach.
    val resultEventBus = remember(::EventBus)
    val parentRouter = LocalRouter.current

    DisposableEffect(router) {
        val prev = parentRouter
        prev?.let(router::bind)
        onDispose { prev?.let(router::unbind) }
    }

    DisposableEffect(router, navigator) {
        router.navigationActionQueue.setNavigator(navigator)
        onDispose(router.navigationActionQueue::removeNavigator)
    }

    // Handle global deep link events.
    if (parentRouter == null)
        GlobalDeepLinkEvents(router) { url -> router.onDeepLink(url) }

    val onBack: () -> Unit = remember(router) {
        { router.pop() }
    }

    val interceptionEnabled = parentRouter != null

    CompositionLocalProvider(
        LocalRouter provides router,
        LocalResultStore provides resultStore,
        LocalResultEventBus provides resultEventBus,
    ) {
        BackInterceptionProvider(interceptionEnabled) {
            content(router, navigator.backStack, onBack)
        }
    }
}
