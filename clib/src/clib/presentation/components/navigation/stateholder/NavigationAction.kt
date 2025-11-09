package clib.presentation.components.navigation.stateholder

import clib.presentation.components.navigation.Route

public sealed interface NavigationAction {
    /**
     * Navigate back
     */
    public data object NavigateBack : NavigationAction

    /**
     * Navigate to route
     */
    public data class Navigate(public val route: Route) : NavigationAction

    /**
     * Navigate back to specific route inclusive or exclusive
     */
    public data class NavigateBackTo(
        public val route: Route,
        public val inclusive: Boolean = false,
    ) : NavigationAction

    /**
     * Navigate to route and remove current view from nav stack
     */
    public data class NavigateAndClearCurrent(public val route: Route) : NavigationAction

    /**
     * Navigate to route and remove all previous routes making current one as a top
     */
    public data class NavigateAndClear(public val route: Route, val reset: Boolean = false) : NavigationAction
}
