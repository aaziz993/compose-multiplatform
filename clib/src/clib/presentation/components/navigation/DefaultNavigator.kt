package clib.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import clib.data.type.collections.ToLaunchedEffect
import clib.presentation.components.navigation.viewmodel.NavigationAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

public class DefaultNavigator<Dest : Any>(
    override val startDestination: Dest,
    private val builder: NavOptionsBuilder.(navController: NavHostController) -> Unit = {}
) : Navigator<Dest> {

    private val navigationActions =
        MutableSharedFlow<NavigationAction>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    override fun navigate(navAction: NavigationAction): Boolean = navigationActions.tryEmit(navAction)

    override fun navigateBack(): Boolean = navigate(NavigationAction.NavigateBack)

    override fun navigate(route: String): Boolean = navigate(NavigationAction.Navigation.Navigate(route))

    override fun navigate(route: Dest): Boolean = navigate(NavigationAction.TypeNavigation.Navigate(route))

    override fun navigateBackTo(route: String, inclusive: Boolean, saveState: Boolean): Boolean =
        navigate(NavigationAction.Navigation.NavigateBackTo(route, inclusive, saveState))

    override fun navigateBackTo(route: Dest, inclusive: Boolean, saveState: Boolean): Boolean =
        navigate(NavigationAction.TypeNavigation.NavigateBackTo(route, inclusive, saveState))

    override fun navigateAndClear(route: String): Boolean =
        navigate(NavigationAction.Navigation.NavigateAndClearTop(route))

    override fun navigateAndClear(route: Dest): Boolean =
        navigate(NavigationAction.TypeNavigation.NavigateAndClearTop(route))

    override fun navigateAndClearCurrent(route: String): Boolean =
        navigate(NavigationAction.Navigation.NavigateAndClearCurrent(route))

    override fun navigateAndClearCurrent(route: Dest): Boolean =
        navigate(NavigationAction.TypeNavigation.NavigateAndClearCurrent(route))

    @Composable
    override fun HandleAction(navController: NavHostController): Unit = navigationActions.ToLaunchedEffect { action ->
        when (action) {
            NavigationAction.NavigateUp -> if (!navController.navigateUp()) navigate(startDestination)

            NavigationAction.NavigateBack -> if (!navController.popBackStack()) navigate(startDestination)

            is NavigationAction.Navigation.Navigate -> navController.navigate(
                action.route,
            ) {
                builder(navController)
                action.block(this, navController)
            }

            is NavigationAction.TypeNavigation.Navigate<*> -> navController.navigate(
                action.route,
            ) {
                builder(navController)
                action.block(this, navController)
            }

            is NavigationAction.Navigation.NavigateBackTo -> navController.navigateBackTo(
                action.route,
                action.inclusive,
                action.saveState,
            )

            is NavigationAction.TypeNavigation.NavigateBackTo<*> -> navController.navigateBackTo(
                action.route,
                action.inclusive,
                action.saveState,
            )

            is NavigationAction.Navigation.NavigateAndClearCurrent -> navController.navigate(
                action.route,
                navigateAndClearCurrentNavOptionsBuilder(navController),
            )

            is NavigationAction.TypeNavigation.NavigateAndClearCurrent<*> -> navController.navigate(
                action.route,
                navigateAndClearCurrentNavOptionsBuilder(navController),
            )

            is NavigationAction.Navigation.NavigateAndClearTop -> navController.navigateAndReplaceStartRoute(action.route)

            is NavigationAction.TypeNavigation.NavigateAndClearTop<*> ->
                navController.navigateAndReplaceStartRoute(action.route)
        }

        navigationActions.resetReplayCache()
    }

    public companion object {

        private fun navigateAndClearCurrentNavOptionsBuilder(navController: NavHostController): NavOptionsBuilder.() -> Unit =
            {
                navController.currentBackStackEntry?.destination?.route?.let {
                    popUpTo(it) { inclusive = true }
                }
            }
    }
}

private fun NavHostController.navigateAndReplaceStartRoute(startDestRoute: String) {
    popBackStack(graph.startDestinationRoute!!, true)
    graph.setStartDestination(startDestRoute)
    navigate(startDestRoute)
}

private fun <T : Any> NavHostController.navigateAndReplaceStartRoute(startDestRoute: T) {
    popBackStack(graph.startDestinationRoute!!, true)
    graph.setStartDestination(startDestRoute)
    navigate(startDestRoute)
}

private fun NavHostController.navigateBackTo(
    route: String,
    inclusive: Boolean = false,
    saveState: Boolean
): Boolean =
    popBackStack(route, inclusive, saveState)

private fun <T : Any> NavHostController.navigateBackTo(
    route: T,
    inclusive: Boolean = false,
    saveState: Boolean
): Boolean =
    popBackStack(route, inclusive, saveState)
