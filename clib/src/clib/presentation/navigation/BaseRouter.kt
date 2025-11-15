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
     * Action queue that manages action execution and navigator lifecycle.
     *
     * This is internal to prevent direct access from outside the library,
     * but accessible to the navigation system for setup.
     */
    internal val navigationActionQueue = NavigationActionQueue()

    /**
     * Currently registered navigator back stack.
     */
    public val backStack: List<NavRoute>
        get() = navigationActionQueue.backStack

    /**
     * Back stack current route
     */
    public val currentRoute: NavRoute?
        get() = backStack.lastOrNull()

    /**
     * Variable to check whether back stack has back route.
     */
    public val hasBackRoute: Boolean
        get() = navigationActionQueue.hasBackRoute

    /**
     * Executes one or more navigation actions.
     *
     * This is the primary method for triggering navigation operations.
     * Actions are passed to the action queue, which handles timing and execution.
     *
     * @param actions Variable number of actions to execute
     */
    public fun actions(vararg actions: NavigationAction): Unit = navigationActionQueue.actions(actions)
}
