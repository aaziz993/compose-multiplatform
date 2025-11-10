package clib.presentation.components.navigation.stateholder

import androidx.compose.runtime.mutableStateListOf
import clib.presentation.components.navigation.AuthRoute
import clib.presentation.components.navigation.Route
import klib.data.type.auth.model.Auth

public class NavigationStateHolder(
    private var auth: Auth = Auth(),
    private val publicStartRoute: Route,
    private val authRoute: Route = publicStartRoute,
    private val protectedStartRoute: Route = publicStartRoute,
    private val loginReset: Boolean = false,
    private val logoutReset: Boolean = false,
) {

    private val startRoute: Route
        get() = if (auth.user == null) publicStartRoute else protectedStartRoute

    public val backStack: List<Route>
        field = mutableStateListOf(startRoute)

    private var authRedirectRoute: Route? = null

    public fun hasBackRoute(): Boolean = backStack.size > 1 || backStack[0] != startRoute

    public fun action(action: NavigationAction) {
        when (action) {
            NavigationAction.NavigateBack ->
                if (backStack.size > 1) backStack.removeLast() else backStack[0] = startRoute

            is NavigationAction.Navigate -> {
                if (action.route.navRoute.isAuth(auth)) add(action.route)
                else {
                    // Store the intended destination and redirect to login.
                    authRedirectRoute = action.route
                    return add(authRoute)
                }
            }

            is NavigationAction.NavigateBackTo -> {
                val index = backStack.indexOf(action.route)
                if (index == -1) return
                val fromIndex = if (action.inclusive) index else index + 1
                if (fromIndex == 0) navigateAndClear(action.route)
                else backStack.removeRange(fromIndex, backStack.size)
            }

            is NavigationAction.NavigateAndClearCurrent -> backStack[backStack.lastIndex] = action.route

            is NavigationAction.NavigateAndClear -> navigateAndClear(action.route)

            is NavigationAction.NavigateAuth -> {
                auth = action.auth
                if (auth.user == null)
                    auth(publicStartRoute, logoutReset) { route -> route.navRoute.authResource() != null }
                else auth(authRedirectRoute ?: startRoute, loginReset) { route -> route is AuthRoute }
            }
        }
        authRedirectRoute = null
    }

    private fun add(route: Route) {
        backStack.add(route)
        val index = backStack.indexOf(route)
        if (index != -1 && index != backStack.lastIndex) backStack.removeAt(index)
    }

    private fun navigateAndClear(route: Route) {
        backStack[0] = route
        if (backStack.size > 1) backStack.removeRange(1, backStack.size)
    }

    private fun auth(route: Route, reset: Boolean, predicate: (Route) -> Boolean) =
        if (reset) navigateAndClear(route)
        else {
            add(route)
            backStack.removeAll(predicate)
        }
}
