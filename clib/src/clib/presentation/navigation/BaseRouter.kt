package clib.presentation.navigation

/**
 * Base class for all router implementations.
 *
 * Provides common functionality for managing navigation actions through a action queue system.
 * Subclasses implement specific navigation methods that create and execute appropriate actions.
 *
 */
public abstract class BaseRouter {

    /**
     *  The top level route.
     */
    public abstract val routes: Routes

    /**
     * Callback to be called if route isn't in the current top level route.
     */
    protected abstract val onUnknownNavRoute: (NavRoute) -> Unit

    /**
     * Action queue that manages action execution and navigator lifecycle.
     *
     * This is internal to prevent direct access from outside the library,
     * but accessible to the navigation system for setup.
     */
    internal val navigationActionQueue: NavigationActionQueue = NavigationActionQueue(onUnknownNavRoute)

    /**
     * Currently registered navigator back stack.
     */
    public val backStack: List<NavRoute>
        get() = navigationActionQueue.attachedNavigator?.backStack ?: emptyList()

    /**
     * Executes one or more navigation actions.
     *
     * This is the primary method for triggering navigation operations.
     * Actions are passed to the action queue, which handles timing and execution.
     *
     * @param actions Variable number of actions to execute
     */
    public fun actions(vararg actions: NavigationAction): Unit =
        navigationActionQueue.actions(*actions)
}
