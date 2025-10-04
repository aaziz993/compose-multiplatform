package clib.presentation.event.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController
import clib.presentation.components.navigation.NavigationRoute

@Immutable
public interface Navigator<Route : NavigationRoute<Route, *>, Dest : NavigationRoute<Route, Dest>> {

    public val startDestination: Dest

    public fun navigate(navAction: NavigationAction): Boolean

    public fun navigateBack(): Boolean

    public fun navigate(route: String): Boolean

    public fun navigate(route: NavigationRoute<Route, *>): Boolean

    public fun navigateBackTo(route: String, inclusive: Boolean = false, saveState: Boolean = false): Boolean

    public fun navigateBackTo(route: NavigationRoute<Route, *>, inclusive: Boolean = false, saveState: Boolean = false): Boolean

    public fun navigateAndClear(route: String): Boolean

    public fun navigateAndClear(route: NavigationRoute<Route, *>): Boolean

    public fun navigateAndClearCurrent(route: String): Boolean

    public fun navigateAndClearCurrent(route: NavigationRoute<Route, *>): Boolean

    @Composable
    public fun HandleAction(navController: NavHostController)
}
