package clib.presentation.components.navigation.stateholder

import androidx.compose.runtime.mutableStateListOf
import clib.presentation.components.navigation.Route

public class NavigationStateHolder(
    startRoute: Route,
) {

    public var startRoute: Route = startRoute
        private set

    public val backStack: List<Route>
        field = mutableStateListOf(startRoute)

    public fun canNavigateBack(): Boolean = backStack.size > 1 || backStack[0] != startRoute

    @Suppress("UNCHECKED_CAST")
    public fun action(action: NavigationAction) {
        when (action) {
            NavigationAction.NavigateBack -> {
                if (backStack.size > 1) backStack.removeLast()
                else if (backStack.first() != startRoute) backStack[0] = startRoute
            }

            is NavigationAction.Navigate ->
                if (backStack.last() != action.route) {
                    val index = backStack.indexOf(action.route)

                    if (index != -1) {
                        backStack[index] = backStack[backStack.lastIndex].also {
                            backStack[backStack.lastIndex] = backStack[index]
                        }
                        return
                    }

                    backStack.add(action.route)
                }

            is NavigationAction.NavigateBackTo -> {
                val index = backStack.indexOf(action.route)
                if (index == -1) return
                val fromIndex = if (action.inclusive) index else index + 1
                if (fromIndex == 0) navigateAndClear(action.route)
                else backStack.removeRange(fromIndex, backStack.size)
            }

            is NavigationAction.NavigateAndClearCurrent ->
                backStack[backStack.lastIndex] = action.route

            is NavigationAction.NavigateAndClear -> {
                if (action.reset) startRoute = action.route
                navigateAndClear(action.route)
            }
        }
    }

    private fun navigateAndClear(route: Route) {
        backStack[0] = route
        backStack.removeRange(1, backStack.size)
    }
}
