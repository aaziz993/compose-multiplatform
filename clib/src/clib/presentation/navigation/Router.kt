package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import klib.data.type.collections.linkedlist.model.Node
import klib.data.type.collections.list.drop

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
internal val LocalRouter: ProvidableCompositionLocal<Router?> = compositionLocalOf { null }

/**
 * Main router implementation providing high-level navigation operations.
 *
 * Router offers a convenient API for common navigation patterns like push, pop, replace,
 * and stack manipulation. It translates these operations into appropriate Command objects
 * and executes them through the action queue system.
 *
 * This class is designed to be used as the primary navigation interface in applications.
 *
 * @param routes The current top level route.
 * @param onReroute The callback to handle reroute of route to related top level router.
 */
public open class Router(
    override val routes: Routes,
    public val onReroute: Router.(NavRoute) -> Unit = Router::push,
) : BaseRouter(), Node<Router> {

    /**
     * Parent router in nested navigation hierarchy.
     */
    override var prev: Router? by mutableStateOf(null)
        private set

    /**
     * Child router in nested navigation hierarchy.
     */
    override var next: Router? by mutableStateOf(null)
        private set

    /**
     * Currently registered navigator's can pop back.
     */
    public val canPopBack: Boolean
        get() = backStack.size > 1 || prev?.canPopBack == true

    /**
     * Nested nav route path.
     */
    public var navRoutePath: List<NavRoute> = emptyList()
        private set

    /**
     * Callback to be called if route isn't in the current top level route.
     */
    override val onUnknownNavRoute: (NavRoute) -> Unit
        get() = { navRoute ->
            routes.navRoutePath(navRoute)?.let {
                navRoutePath = it.drop()
                onReroute(it.first())
            } ?: prev?.onUnknownNavRoute(navRoute)
        }

    /**
     * Creates parent child routers relationship.
     */
    public fun bind(parentRouter: Router) {
        require(this != parentRouter) { "Router can't be parent of itself" }

        parentRouter.next = this
        prev = parentRouter
        navRoutePath = parentRouter.navRoutePath.drop()
    }

    /**
     * Pushes one or more routes onto the navigation stack.
     *
     * Each route will be added to the top of the stack in the order provided.
     * This allows for building deep navigation chains in a single operation.
     *
     * @param routes Variable number of routes to push onto the stack.
     * @throws IllegalArgumentException if no routes are provided.
     */
    public fun push(vararg routes: NavRoute) {
        require(routes.isNotEmpty()) { "Screens must not be empty" }

        val actions = routes.map(NavigationAction::Push).toTypedArray()
        actions(*actions)
    }

    /**
     * Replaces the current top route with a new route.
     *
     * If the stack is empty, the new route will be added as the first route.
     * This is useful for scenarios like login flow completion or error recovery.
     *
     * @param route The route to replace the current top route with.
     */
    public fun replaceCurrent(route: NavRoute): Unit =
        actions(NavigationAction.ReplaceCurrent(route))

    /**
     * Replaces the entire navigation stack with new routes.
     *
     * Useful for major navigation flow changes like switching between authenticated/unauthenticated states.
     *
     * @param routes Variable number of routes to replace the stack with.
     * @triggers system back navigation when the stack is empty.
     */
    public fun replaceStack(vararg routes: NavRoute): Unit =
        actions(NavigationAction.ReplaceStack(routes.toList()))

    /**
     * Removes the top route from the navigation stack.
     *
     * If only one route remains, this will trigger system back navigation.
     * (typically exiting the app or returning to the previous activity).
     */
    public fun pop(): Unit = actions(NavigationAction.Pop)

    /**
     * Navigates back to a specific route in the stack.
     *
     * Removes all routes above the target route. If the target route is not found,
     * all routes except the root will be removed.
     *
     * @param route The target route to navigate back to.
     */
    public fun popTo(route: NavRoute): Unit = actions(NavigationAction.PopTo(route))

    /**
     * Clears all routes except the root route.
     *
     * This resets the navigation stack to its initial state while preserving the root.
     * Useful for "back to main" functionality or clearing complex navigation flows.
     */
    public fun resetToRoot(): Unit = actions(NavigationAction.ResetToRoot)

    /**
     * Removes all routes except the current top route.
     *
     * The current route becomes the new root of the stack and
     * system back navigation will be triggered.
     *
     * Use it when you want to close a nested navigation graph or a whole application.
     */
    public fun dropStack(): Unit = actions(NavigationAction.DropStack)
}

/**
 * Creates and remembers a Router instance.
 *
 * The router will be recreated if any of the provided keys change, allowing for
 * state-dependent router configurations.
 *
 * @param keys Optional keys that trigger router recreation when changed.
 * @param factory Factory function to create the router instance.
 * @return A remembered router instance.
 */
@Composable
public fun rememberRouter(
    routes: Routes,
    vararg keys: Any?,
    factory: () -> Router = { Router(routes) },
): Router = remember(*keys) { factory() }

@Composable
public fun currentRouter(): Router =
    checkNotNull(LocalRouter.current) { "No Router was provided via LocalRouter" }
