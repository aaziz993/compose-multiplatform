package clib.presentation.event.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController
import clib.presentation.components.navigation.model.NavigationEndpoint

@Immutable
public interface Navigator<T : NavigationEndpoint> {

    public val startDestination: T

    public fun navigate(navAction: NavigationAction): Boolean

    public fun navigateBack(): Boolean

    public fun navigate(route: String): Boolean

    public fun navigate(route: T): Boolean

    public fun navigateBackTo(route: String, inclusive: Boolean = false, saveState: Boolean = false): Boolean

    public fun navigateBackTo(route: T, inclusive: Boolean = false, saveState: Boolean = false): Boolean

    public fun navigateAndClear(route: String): Boolean

    public fun navigateAndClear(route: T): Boolean

    public fun navigateAndClearCurrent(route: String): Boolean

    public fun navigateAndClearCurrent(route: T): Boolean

    @Composable
    public fun HandleAction(navController: NavHostController)
}
