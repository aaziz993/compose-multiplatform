package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Main router implementation providing high-level navigation operations.
 *
 * Router offers a convenient API for common navigation patterns like push, pop, replace,
 * and stack manipulation. It translates these operations into appropriate Command objects
 * and executes them through the action queue system.
 *
 * This class is designed to be used as the primary navigation interface in applications.
 *
 */
public open class Router() : BaseRouter() {

    /**
     * Pushes one or more routes onto the navigation stack.
     *
     * Each route will be added to the top of the stack in the order provided.
     * This allows for building deep navigation chains in a single operation.
     *
     * @param routes Variable number of routes to push onto the stack
     * @throws IllegalArgumentException if no routes are provided
     */
    public fun push(vararg routes: NavRoute) {
        require(routes.isNotEmpty()) { "Screens must not be empty" }

        val actions = routes.map(NavigationAction::Push).toTypedArray()
        navigationActionQueue.actions(*actions)
    }

    /**
     * Replaces the current top route with a new route.
     *
     * If the stack is empty, the new route will be added as the first route.
     * This is useful for scenarios like login flow completion or error recovery.
     *
     * @param route The route to replace the current top route with
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
     * If only one route remains, this will trigger system back navigation
     * (typically exiting the app or returning to the previous activity).
     */
    public fun pop(): Unit = actions(NavigationAction.Pop)

    /**
     * Navigates back to a specific route in the stack.
     *
     * Removes all routes above the target route. If the target route is not found,
     * all routes except the root will be removed.
     *
     * @param route The target route to navigate back to
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
 * @param keys Optional keys that trigger router recreation when changed
 * @param factory Factory function to create the router instance
 * @return A remembered router instance
 */
@Composable
public fun rememberRouter(
    vararg keys: Any?,
    factory: () -> Router = { Router() },
): Router = remember(*keys) { factory() }
