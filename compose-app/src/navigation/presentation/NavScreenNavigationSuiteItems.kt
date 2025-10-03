package navigation.presentation

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
    Destination.destinations.forEach { destination ->
        val navItem = destination.item(label)
        val selected = currentDestination?.hierarchy?.any { it.hasRoute(destination::class) } == true
        item(
            selected,
            {
                navController.navigate(destination) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.startDestinationRoute!!) {
                        saveState = true
                    }

                    // Avoid multiple copies of the same destination when
                    // re-selecting the same item
                    launchSingleTop = true
                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                }
            },
            { navItem.Icon(selected = selected) },
            navItem.modifier(selected),
            navItem.enabled,
            { navItem.Text(selected = selected) },
            navItem.alwaysShowLabel,
            { navItem.Badge(selected = selected) },
        )
    }
}
