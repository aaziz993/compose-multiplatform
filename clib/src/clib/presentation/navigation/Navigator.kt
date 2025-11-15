package clib.presentation.navigation

/**
 * Core interface for navigation execution.
 *
 * Navigator is responsible for applying navigation commands to the actual navigation stack.
 * It acts as a bridge between the high-level Router API and the underlying navigation system.
 *
 */
public interface Navigator {

    public val backStack: List<NavRoute>

    public val hasBackRoute: Boolean

    /**
     * Applies an array of navigation commands to the navigation stack.
     *
     * @param actions Array of actions to apply to the navigation stack
     */
    public fun actions(actions: Array<out NavigationAction>)
}
