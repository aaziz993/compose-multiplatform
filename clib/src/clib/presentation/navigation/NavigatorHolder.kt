package clib.presentation.navigation

/**
 * Interface for managing the lifecycle of Navigator instances.
 *
 * NavigatorHolder is used to decouple navigation action generation from navigation execution.
 * This allows actions to be queued when no navigator is available and executed later
 * when a navigator becomes available.
 *
 */
public interface NavigatorHolder {

    /**
     * Binds a navigator instance to handle navigation actions.
     *
     * @param navigator The navigator instance to register.
     */
    public fun bindNavigator(navigator: Navigator)

    /**
     * Unbinds the current navigator.
     *
     */
    public fun unbindNavigator()
}
