package clib.presentation.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Manages queuing and execution of navigation actions.
 *
 * CommandQueue solves the timing problem where navigation actions might be issued
 * before the navigation system is ready(e.g. changing configuration).
 * It stores actions when no navigator is available and executes them
 * immediately when a navigator becomes available.
 *
 * All action execution happens on the Main dispatcher to ensure UI thread safety.
 */
public class NavigationActionQueue : NavigatorHolder {

    /** Currently registered navigator, null if none is set */
    private var _navigator: Navigator? by mutableStateOf(null)

    /**
     * Currently registered navigator back stack.
     */
    internal val backStack: List<NavRoute> by derivedStateOf {
        _navigator?.backStack ?: emptyList()
    }

    /**
     *  Checks does back stack has back route.
     */
    internal val hasBackRoute: Boolean by derivedStateOf {
        _navigator?.hasBackRoute ?: false
    }

    /** Queue of pending actions waiting for a navigator to become available */
    private val pendingActions = mutableListOf<Array<out NavigationAction>>()

    /** Coroutine scope for executing actions on the main thread */
    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Sets the navigator and executes any pending actions.
     *
     * When a navigator is registered, all queued actions are immediately
     * executed in the order they were added to the queue.
     *
     * @param navigator The navigator to register
     */
    override fun setNavigator(navigator: Navigator) {
        _navigator = navigator
        if (pendingActions.isNotEmpty()) {
            val snapshot = pendingActions.toList()
            pendingActions.clear()
            snapshot.forEach(navigator::actions)
        }
    }

    /**
     * Removes the current navigator.
     *
     * After calling this, new actions will be queued until a new navigator is set.
     */
    override fun removeNavigator() {
        _navigator = null
    }

    /**
     * Executes navigation actions immediately or queues them if no navigator is available.
     *
     * This method ensures that actions are always executed on the main thread,
     * making it safe to call from any thread context.
     *
     * @param actions Array of actions to execute
     */
    public fun actions(actions: Array<out NavigationAction>) {
        mainScope.launch {
            _navigator?.actions(actions) ?: pendingActions.add(actions)
        }
    }
}
