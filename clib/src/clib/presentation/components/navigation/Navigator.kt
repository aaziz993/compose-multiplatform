package clib.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController
import clib.presentation.components.navigation.viewmodel.NavigationAction

@Immutable
public interface Navigator<Dest : Any> {

    public val startDestination: Dest

    public fun navigate(navAction: NavigationAction): Boolean

    public fun navigateBack(): Boolean

    public fun navigate(route: String): Boolean

    public fun navigate(route: Dest): Boolean

    public fun navigateBackTo(route: String, inclusive: Boolean = false, saveState: Boolean = false): Boolean

    public fun navigateBackTo(route: Dest, inclusive: Boolean = false, saveState: Boolean = false): Boolean

    public fun navigateAndClear(route: String): Boolean

    public fun navigateAndClear(route: Dest): Boolean

    public fun navigateAndClearCurrent(route: String): Boolean

    public fun navigateAndClearCurrent(route: Dest): Boolean

    @Composable
    public fun HandleAction(navController: NavHostController)
}
