package clib.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Manages queuing and execution of navigation actions.
 *
 * NavigationActionQueue solves the timing problem where navigation actions might be issued
 * before the navigation system is ready(e.g. changing configuration).
 * It stores actions when no navigator is available and executes them
 * immediately when a navigator becomes available.
 *
 * All action execution happens on the Main dispatcher to ensure UI thread safety.
 * @param onUnknownNavRoute Callback to be called if route isn't in the current top level route.
 */
public class NavigationActionQueue(
    private val onUnknownNavRoute: (NavRoute) -> Unit,
) : NavigatorHolder {

    /** Currently attached navigator, null if none is set. */
    internal var attachedNavigator: Navigator? by mutableStateOf(null)
        private set

    /** Queue of pending actions waiting for a navigator to become available. */
    private val pendingActions = mutableListOf<Array<out NavigationAction>>()

    /** Coroutine scope for executing actions on the main thread. */
    private val mainScope = MainScope()

    /**
     * Sets the navigator and executes any pending actions.
     *
     * When a navigator is registered, all queued actions are immediately
     * executed in the order they were added to the queue.
     *
     * @param navigator The navigator to register
     */
    override fun setNavigator(navigator: Navigator) {
        attachedNavigator = navigator
        if (pendingActions.isNotEmpty()) {
            val snapshot = pendingActions.toList()
            pendingActions.clear()
            snapshot.forEach { actions -> navigator.actions(actions = actions, onUnknownNavRoute = onUnknownNavRoute) }
        }
    }

    /**
     * Removes the current navigator.
     *
     * After calling this, new actions will be queued until a new navigator is set.
     */
    override fun removeNavigator() {
        attachedNavigator = null
    }

    /**
     * Executes navigation actions immediately or queues them if no navigator is available.
     *
     * This method ensures that actions are always executed on the main thread,
     * making it safe to call from any thread context.
     *
     * @param actions Array of actions to execute.
     */
    public fun actions(vararg actions: NavigationAction) {
        mainScope.launch {
            attachedNavigator?.actions(actions = actions, onUnknownNavRoute = onUnknownNavRoute)
                ?: pendingActions.add(actions)
        }
    }
}
