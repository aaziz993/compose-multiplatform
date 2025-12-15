package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import klib.data.auth.model.Auth
import klib.data.type.collections.replaceWith
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

/**
 * Navigator implementation that integrates with Navigation 3.
 *
 * This class bridges the gap between the high-level Router API and the underlying
 * Navigation 3 system. It translates navigation actions into direct manipulations
 * of the Navigation 3 back stack.
 *
 * @param routes The top level route associated with navigator.
 * @param backStack The Navigation 3 back stack to control.
 * @param authRoute Optional route representing an authentication screen. If provided, navigator may redirect unauthorized users here.
 * @param authRedirectRoute Optional route the navigator should redirect when authenticated.
 * @param auth The current authentication state used to determine access control.
 * @param onBack Callback to trigger system back navigation when the stack is empty.
 */
public open class Nav3Navigator(
    override val routes: Routes,
    override val backStack: NavBackStack<NavRoute>,
    private val auth: Auth,
    private val authRoute: NavRoute?,
    private var authRedirectRoute: NavRoute?,
    private val onBack: () -> Unit,
    private val onError: (Throwable) -> Unit,
) : Navigator {

    init {
        auth()
    }

    /**
     * Coroutine scope for scheduling back navigation calls.
     */
    private val mainScope = MainScope()

    /**
     * Applies an array of navigation actions to the back stack.
     *
     * Actions are processed sequentially against a snapshot of the current stack.
     * Once all actions are processed, the actual back stack is updated atomically.
     * This ensures consistency and prevents intermediate states from being visible.
     *
     * @param actions Array of navigation actions to apply.
     * @param onUnknownRoute Callback to be called if route isn't in the current top level route.
     */
    override fun actions(
        vararg actions: NavigationAction,
        onUnknownRoute: (NavRoute) -> Unit,
    ) {
        val snapshot = backStack.toMutableList()
        var callOnBack = false

        for (action in actions) {
            try {
                if (!action(
                        snapshot,
                        action,
                        onUnknownRoute,
                    ) { callOnBack = true }
                ) return
            }
            catch (e: RuntimeException) {
                return onError(e)
            }
        }

        swap(snapshot)

        if (callOnBack) scheduleOnBack()
    }

    /**
     * Processes a single navigation action against the stack snapshot.
     *
     * This method can be overridden in subclasses to add custom action types
     * or modify the behavior of existing actions.
     *
     * @param snapshot Mutable copy of the navigation stack.
     * @param action Command to process.
     * @param onUnknownRoute Callback to be called if route isn't in the current top level route.
     * @param onBackRequested Callback to trigger when system back navigation is needed.
     */
    protected open fun action(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction,
        onUnknownRoute: (NavRoute) -> Unit,
        onBackRequested: () -> Unit,
    ): Boolean {
        when (action) {
            is NavigationAction.Push -> return push(snapshot, action, onUnknownRoute)
            is NavigationAction.ReplaceCurrent -> replaceCurrent(snapshot, action)
            is NavigationAction.ReplaceStack -> replaceStack(snapshot, action)
            is NavigationAction.PopTo -> popTo(snapshot, action)
            is NavigationAction.Pop -> if (!pop(snapshot)) onBackRequested()
            is NavigationAction.ResetToRoot -> resetToRoot(snapshot)
            is NavigationAction.DropStack -> {
                dropStack(snapshot)
                onBackRequested()
            }
        }

        return true
    }

    /**
     * Adds a route to the top of the stack.
     *
     * @param snapshot The stack snapshot to modify.
     * @param action The push action containing the route to add.
     * @param onUnknownRoute The callback to be called if route isn't in the current top level route.
     */
    protected open fun push(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.Push,
        onUnknownRoute: (NavRoute) -> Unit,
    ): Boolean {
        // If the user explicitly requested the auth route, don't redirect them after login.
        if (action.route == authRoute) authRedirectRoute = null

        val route = if (action.route.route.isAuth(auth)) action.route
        else {
            check(auth.user == null) { "Insufficient user privileges '${auth.user}'" }
            // Store the intended destination and redirect to auth.
            authRedirectRoute = action.route
            checkNotNull(authRoute) { "No auth route" }
        }

        if (route.route !in routes.routes) {
            onUnknownRoute(route)
            return false
        }



        if (!action.duplicate) {
            popTo(snapshot, NavigationAction.PopTo(action.route))
            if (snapshot.lastOrNull() == route) snapshot.removeLast()
        }

        snapshot += route

        return true
    }

    /**
     * Replaces the current top route or adds a route if the stack is empty.
     *
     * @param snapshot The stack snapshot to modify.
     * @param action The replace action containing the new route.
     */
    protected open fun replaceCurrent(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.ReplaceCurrent,
    ) {
        require(action.route.route in routes.routes) {
            "Route '${action.route.route}' isn't in '$routes${routes.routes}'"
        }
        if (snapshot.isEmpty()) snapshot += action.route else snapshot[snapshot.lastIndex] = action.route
    }

    /**
     * Replaces the entire navigation stack with new routes.
     *
     * Useful for major navigation flow changes like switching between authenticated/unauthenticated states.
     *
     * @param routes Variable number of routes to replace the stack with.
     * @throws IllegalArgumentException if no routes are provided.
     */
    protected open fun replaceStack(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.ReplaceStack,
    ) {
        require(action.routes.all { navRoute -> navRoute.route in routes.routes }) {
            "Routes '${action.routes.map(NavRoute::route)}' isn't in '$routes${routes.routes}'"
        }
        snapshot.replaceWith(action.routes)
    }

    /**
     * Replaces routes based on authentication state.
     *
     * When the user is unauthenticated:
     * - Keeps only routes that are allowed for unauthenticated users.
     * - If none remain, falls back to a start route that allows unauthenticated access.
     *
     * When the user is authenticated:
     * - Removes all authentication-only routes.
     * - Appends an auth redirect route if defined.
     *
     * If the resulting stack is non-empty, it replaces the current stack.
     * If empty, parent navigators may handle the transition instead.
     */
    protected open fun auth() {
        val authStack = if (auth.user == null) backStack.filter { navRoute -> navRoute.route.isAuth(auth) }.ifEmpty {
            listOfNotNull(routes.startRoute.takeIf { navRoute -> navRoute.route.isAuth(auth) })
        }
        else backStack.filterNot { navRoute -> navRoute is AuthRoute } + listOfNotNull(authRedirectRoute)
        if (authStack.isNotEmpty()) actions(NavigationAction.ReplaceStack(authStack))
    }

    /**
     * Removes routes until the target route is reached.
     *
     * @param snapshot The stack snapshot to modify.
     * @param action The popTo action containing the target route.
     */
    protected open fun popTo(
        snapshot: MutableList<NavRoute>,
        action: NavigationAction.PopTo,
    ) {
        val idx = snapshot.indexOfFirst { navRoute -> navRoute == action.route }

        if (idx != -1) {
            val from = idx + 1
            if (from < snapshot.size) snapshot.removeRange(from, snapshot.size)
        }
    }

    /**
     * Removes the top route from the stack.
     *
     * @param snapshot The stack snapshot to modify
     * @return true if a route was removed, false if the stack would become empty
     */
    protected open fun pop(
        snapshot: MutableList<NavRoute>,
    ): Boolean {
        val result = snapshot.size > 1

        if (result) snapshot.removeLastOrNull()

        return result
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
     * @param snapshot The stack snapshot to modify.
     */
    protected open fun dropStack(snapshot: MutableList<NavRoute>): Unit = snapshot.removeRange(0, snapshot.lastIndex)

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
     * @param value The new contents for the list.
     */
    public open fun swap(
        value: List<NavRoute>,
    ) {
        Snapshot.withMutableSnapshot {
            backStack.replaceWith(value)
        }
    }

    /**
     * Extension function to remove a range of elements from a MutableList.
     *
     * This is a convenience method that uses the subList approach for efficiency.
     *
     * @param fromIndex Start index (inclusive).
     * @param toIndex End index (exclusive).
     */
    protected open fun MutableList<NavRoute>.removeRange(fromIndex: Int, toIndex: Int) {
        subList(fromIndex, toIndex).clear()
    }
}

/**
 * Creates and remembers a Nav3Navigator bound to the given back stack.
 *
 * The navigator will be recreated only if the back stack reference, onBack or onError callback changes.
 * This ensures proper lifecycle management and prevents memory leaks.
 *
 * @param routes The top level route associated with navigator.
 * @param startRoute Optional route representing an start route. If provided, back stack will start with that.
 * @param authRoute Optional route representing an authentication route. If provided, navigator may redirect unauthorized users here.
 * @param authRedirectRoute Optional route the navigator should redirect when authenticated.
 * @param auth The current authentication state used to determine access control.
 * @param onBack Callback to trigger system back navigation when the stack is empty.
 * @return A remembered navigator instance.
 */
@Composable
public fun rememberNav3Navigator(
    routes: Routes,
    startRoute: NavRoute? = LocalRouter.current?.routePath?.firstOrNull(),
    auth: Auth = Auth(),
    authRoute: NavRoute? = null,
    authRedirectRoute: NavRoute? = null,
    onBack: () -> Unit = LocalRouter.current?.let { it::pop } ?: platformOnBack(),
    onError: (Throwable) -> Unit = { e ->
        nav3Logger.error(e.cause, Nav3Navigator::class.simpleName!!) { e.message }
    },
): Navigator {
    val backStack = rememberNavBackStack(routes, startRoute)

    LaunchedEffect(backStack.toList()) {
        nav3Logger.debug(Nav3Navigator::class.simpleName!!) { "Back stack: ${backStack.joinToString(" -> ")}" }
    }

    return remember(auth) {
        Nav3Navigator(
            routes,
            backStack,
            auth,
            authRoute,
            authRedirectRoute,
            onBack,
            onError,
        )
    }
}
