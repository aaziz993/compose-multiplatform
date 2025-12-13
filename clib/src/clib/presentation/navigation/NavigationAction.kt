package clib.presentation.navigation

/**
 * Base interface for all navigation NavigationActions.
 * Actions represent different navigation operations that can be applied to the navigation stack.
 *
 */
public interface NavigationAction {

    /**
     * Action to push a new route onto the navigation stack.
     * The route will be added to the top of the stack.
     *
     * @param route The route to push onto the stack.
     */
    public data class Push(val route: NavRoute) : NavigationAction

    /**
     * Action to replace the current top route with a new one.
     * If the stack is empty, the route will be added as the first route.
     *
     * @param route The route to replace the current top route with
     */
    public data class ReplaceCurrent(val route: NavRoute) : NavigationAction

    /**
     * Replaces the entire navigation stack with new routes.
     *
     * Useful for major navigation flow changes like switching between authenticated/unauthenticated states.
     *
     * @param routes Variable number of routes to replace the stack with.
     * @throws IllegalArgumentException if no routes are provided.
     */
    public data class ReplaceStack(val routes: List<NavRoute>) : NavigationAction

    /**
     * Action to pop (remove) the top route from the navigation stack.
     * If only one route remains, this will trigger system back navigation.
     */
    public data object Pop : NavigationAction

    /**
     * Action to pop all routes until the specified route is reached.
     * If the target route is not found, all routes except the root will be removed.
     *
     * @param route The target route to navigate back to
     */
    public data class PopTo(val route: NavRoute) : NavigationAction

    /**
     * Action to clear all routes except the root route.
     * This effectively resets the navigation stack to its initial state.
     */
    public data object ResetToRoot : NavigationAction

    /**
     * Action to remove all routes except the current top route.
     * This creates a new stack with only the current route as the root.
     * After this NavigationAction, system back navigation will be triggered.
     */
    public data object DropStack : NavigationAction
}
