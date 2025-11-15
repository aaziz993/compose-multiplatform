package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import klib.data.type.auth.model.Auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import pro.respawn.kmmutils.common.replaceWith

/**
 * Navigator implementation that integrates with Jetpack Navigation 3.
 *
 * This class bridges the gap between the high-level Router API and the underlying
 * Navigation 3 system. It translates navigation actions into direct manipulations
 * of the Navigation 3 back stack.
 *
 * @param navBackStack The Navigation 3 back stack to manipulate
 * @param authRoute The route that users should be taken to when they attempt to access a route that requires login
 * @param auth
 * @param onBack Callback triggered when navigation is pretend to become empty
 * @param onError Callback triggered when navigation action throws error
 */
public open class Nav3Navigator(
    private var auth: Auth = Auth(),
    private val protectedRoute: NavRoute,
    private val authRoute: NavRoute = protectedRoute,
    private val publicRoute: NavRoute = authRoute,
    private val navBackStack: NavBackStack<NavRoute>,
    private val onBack: () -> Unit = {},
    private val onError: suspend (NavigationException) -> Unit = { },
) : Navigator {

    init {
        if (navBackStack.isEmpty()) navBackStack += startRoute
    }

    /** Coroutine scope for scheduling back navigation calls */
    protected val mainScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val startRoute: NavRoute
        get() = if (auth.user == null) publicRoute else protectedRoute

    override val backStack: List<NavRoute> = navBackStack

    override val hasBackRoute: Boolean
        get() = backStack.size > 1 || backStack.first() != startRoute

    private var authRedirectRoute: NavRoute? = null

    /**
     * Applies an array of navigation actions to the back stack.
     *
     * Commands are processed sequentially against a snapshot of the current stack.
     * Once all actions are processed, the actual back stack is updated atomically.
     * This ensures consistency and prevents intermediate states from being visible.
     *
     * @param actions Array of navigation actions to apply
     */
    override fun actions(vararg actions: NavigationAction) {
        val snapshot = navBackStack.toMutableList()
        var callOnBack = false

        for (action in actions) {
            try {
                action(
                    snapshot = snapshot,
                    action = action,
                    onFinishRequested = {
                        callOnBack = true
                    },
                )
            }
            catch (e: RuntimeException) {
                mainScope.launch {
                    onError(NavigationException(action, e))
                }
            }
        }

        navBackStack.swap(snapshot)

        if (callOnBack) scheduleOnBack()
    }

    /**
     * Processes a single navigation action against the stack snapshot.
     *
     * This method can be overridden in subclasses to add custom action types
     * or modify the behavior of existing actions.
     *
     * @param snapshot Mutable copy of the navigation stack
     * @param action Command to process
     * @param onFinishRequested Callback to trigger when system back navigation is needed
     */
    protected open fun action(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction,
        onFinishRequested: () -> Unit,
    ) {
        when (action) {
            is NavigationAction.Push -> push(snapshot, action)
            is NavigationAction.ReplaceCurrent -> replace(snapshot, action)
            is NavigationAction.ReplaceStack -> replace(snapshot, action)
            is NavigationAction.PopTo -> popTo(snapshot, action)
            is NavigationAction.Pop -> if (!pop(snapshot)) onFinishRequested()
            is NavigationAction.ResetToRoot -> resetToRoot(snapshot)
            is NavigationAction.DropStack -> dropStack(snapshot)
            is NavigationAction.AuthStack -> authStack(snapshot, action)
        }
    }

    /**
     * Adds a route to the top of the stack.
     *
     * @param snapshot The stack snapshot to modify
     * @param action The push action containing the route to add
     */
    protected open fun push(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.Push,
    ) {
        // If the user explicitly requested the auth route, don't redirect them after login.
        if (action.route == authRoute) authRedirectRoute = null

        val route = if (action.route.route.isAuth(auth)) action.route
        else {
            // Store the intended destination and redirect to auth.
            authRedirectRoute = action.route
            authRoute
        }

        if (action.popTo) {
            val idx = popTo(snapshot, NavigationAction.PopTo(route, true))
            if (idx != -1) {
                snapshot[idx] = route
                return
            }
        }

        snapshot.addOrSet(route)
    }

    /**
     * Replaces the current top route or adds a route if the stack is empty.
     *
     * @param snapshot The stack snapshot to modify
     * @param action The replace action containing the new route
     */
    protected open fun replace(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.ReplaceCurrent,
    ) {
        if (snapshot.isEmpty()) snapshot += action.route
        else snapshot[snapshot.indices.last] = action.route
    }

    /**
     * Replaces the current stack with new.
     *
     * @param snapshot The stack snapshot to modify
     * @param action The replace action containing the new stack
     */
    protected open fun replace(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.ReplaceStack,
    ) {
        require(action.routes.isNotEmpty()) { "Stack is empty" }

        snapshot.replaceWith(action.routes)
    }

    /**
     * Removes routes until the target route is reached.
     *
     * If the target route is not found, all routes except the root are removed.
     *
     * @param snapshot The stack snapshot to modify
     * @param action The popTo action containing the target route
     */
    protected open fun popTo(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.PopTo,
    ): Int {
        val target = action.route
        val idx = snapshot.indexOfFirst { route -> route == target }

        return if (idx == -1) idx
        else {
            val from = if (action.inclusive) idx else idx + 1
            if (from < snapshot.size) snapshot.removeRange(from, snapshot.size)
            if (snapshot.isEmpty()) snapshot += startRoute
            from
        }
    }

    /**
     * Removes the top route from the stack.
     *
     * @param snapshot The stack snapshot to modify
     * @return true if a route was removed, false if the stack would become empty
     */
    protected open fun pop(snapshot: MutableList<NavRoute>): Boolean {
        when {
            snapshot.size > 1 -> snapshot.removeLastOrNull()
            snapshot.first() != startRoute -> snapshot[0] = startRoute

            else -> return false
        }

        return true
    }

    /**
     * Removes all routes except the root.
     *
     * @param snapshot The stack snapshot to modify
     */
    protected open fun resetToRoot(snapshot: MutableList<NavRoute>) {
        if (snapshot.size > 1) snapshot.removeRange(1, snapshot.size)
    }

    /**
     * Removes all routes except the current top route.
     *
     * This function is useful when you want to close a nested navigation graph or
     * a whole application.
     *
     * @param snapshot The stack snapshot to modify
     */
    protected open fun dropStack(snapshot: MutableList<NavRoute>) {
        snapshot.removeRange(0, snapshot.lastIndex)
        snapshot += startRoute
    }

    /**
     * Switches between authenticated/unauthenticated stacks.
     *
     */
    private fun authStack(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.AuthStack,
    ) {
        auth = action.auth

        val route = if (auth.user == null) {
            snapshot.removeAll { navRoute -> !navRoute.route.isAuth(auth) }
            publicRoute
        }
        else {
            snapshot.removeAll { navRoute -> navRoute is AuthRoute }
            (authRedirectRoute ?: protectedRoute).also { authRedirectRoute = null }
        }

        snapshot.addOrSet(route)
    }

    /**
     * Schedules a system back navigation call.
     *
     * The call is delayed using yield() to ensure that NavDisplay has received
     * the updated back stack before the system back navigation is triggered.
     * This prevents race conditions where the system tries to handle back
     * navigation before the UI has been updated.
     */
    protected open fun scheduleOnBack() {
        mainScope.launch {
            yield()
            onBack()
        }
    }

    /**
     * Atomically replaces the contents of a SnapshotStateList.
     *
     * Uses Compose's snapshot system to ensure the update is atomic and
     * properly triggers recomposition.
     *
     * @param value The new contents for the list
     */
    protected fun NavBackStack<NavRoute>.swap(
        value: List<NavRoute>,
    ) {
        Snapshot.withMutableSnapshot {
            clear()
            addAll(value)
        }
    }

    /**
     * Extension function to remove a range of elements from a MutableList.
     *
     * This is a convenience method that uses the subList approach for efficiency.
     *
     * @param fromIndex Start index (inclusive)
     * @param toIndex End index (exclusive)
     */
    protected fun MutableList<NavRoute>.removeRange(fromIndex: Int, toIndex: Int): Unit =
        subList(fromIndex, toIndex).clear()

    private fun MutableList<NavRoute>.addOrSet(route: NavRoute) =
        if (lastOrNull() == route) this[lastIndex] = route
        else this += route
}

/**
 * Creates and remembers a Nav3Navigator bound to the given back stack.
 *
 * The navigator will be recreated only if the back stack reference or onBack callback changes.
 * This ensures proper lifecycle management and prevents memory leaks.
 *
 * @param backStack The Navigation 3 back stack to control
 * @param onBack Callback triggered when back stack pretend to become empty
 * @param onNavigationError Callback triggered when navigation throws exception
 * @return A remembered navigator instance
 */
@Suppress("ComposeParameterOrder")
@Composable
public fun rememberNav3Navigator(
    auth: Auth = Auth(),
    protectedRoute: NavRoute,
    authRoute: NavRoute = protectedRoute,
    publicRoute: NavRoute = authRoute,
    backStack: NavBackStack<NavRoute>,
    onBack: () -> Unit = platformOnBack(),
    onNavigationError: suspend (NavigationException) -> Unit = { },
): Navigator = remember(auth, backStack) {
    Nav3Navigator(auth, protectedRoute, authRoute, publicRoute, backStack, onBack, onNavigationError)
}
