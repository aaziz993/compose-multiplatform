package presentation.navigation.presentation

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import clib.presentation.components.navigation.model.AbstractDestination

public fun navScreenNavigationSuiteItems(
    navController: NavHostController,
    currentDestination: NavDestination?,
    label: AbstractDestination.() -> String = { this::class.simpleName!! },
): NavigationSuiteScope.() -> Unit = {

}
