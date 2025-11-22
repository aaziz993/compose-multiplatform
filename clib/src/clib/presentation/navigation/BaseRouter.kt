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

    /** The current top level route. */
    public val routes: Routes?
        get() = navigationActionQueue.navigators.lastOrNull()?.routes

    /**
     * Currently registered navigator back stack.
     */
    public val backStack: List<NavRoute>
        get() = navigationActionQueue.navigators.lastOrNull()?.backStack ?: emptyList()

    /**
     * Currently registered navigators has back.
     */
    public val hasBack: Boolean
        get() = navigationActionQueue.navigators.any { navigator -> navigator.backStack.size > 1 }

    /**
     * Executes one or more navigation actions.
     *
     * This is the primary method for triggering navigation operations.
     * Actions are passed to the action queue, which handles timing and execution.
     *
     * @param actions Variable number of actions to execute
     */
    public fun actions(vararg actions: NavigationAction): Unit = navigationActionQueue.actions(*actions)
}
