package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import clib.presentation.event.navigator.NavigationAction
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

public abstract class AbstractDestination<T : NavigationNode<T>> : NavigationNode<T> {

    public val label: String
        get() = this::class.simpleName!!
    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    protected open val modifier: Modifier = Modifier
    protected open val selectedModifier: Modifier = modifier
    protected open val enabled: Boolean = true
    protected open val alwaysShowLabel: Boolean = true

    @Composable
    protected open fun Text(label: String, modifier: Modifier = Modifier): Unit =
        androidx.compose.material3.Text(text = label)

    @Composable
    protected open fun SelectedText(label: String, modifier: Modifier = Modifier): Unit =
        androidx.compose.material3.Text(text = label)

    @Composable
    protected open fun Icon(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun SelectedIcon(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun Badge(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun SelectedBadge(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun Screen(route: T, navigateTo: (route: AbstractDestination<T>) -> Unit = {}, navigateBack: () -> Unit = {}): Unit = Unit

    context(navGraphBuilder: NavGraphBuilder)
    override fun item(
        typeMap: Map<KType, NavType<*>>,
        deepLinks: List<String>,
        enterTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition),
        exitTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition),
        popEnterTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition),
        popExitTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition),
        sizeTransform:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?,
        navigateTo: (NavBackStackEntry, route: AbstractDestination<T>) -> Unit,
        navigateBack: (NavBackStackEntry) -> Unit
    ): Unit = with(navGraphBuilder) {
        val deepDeepLinks = deepDeepLinks(deepLinks)
        composable(
            this::class,
            typeMap + this@AbstractDestination.typeMap,
            deepDeepLinks.map { basePath ->
                navDeepLink(this::class, basePath) {}
            },
            enterTransition,
            exitTransition,
            popEnterTransition,
            popExitTransition,
            sizeTransform,
        ) { backStackEntry ->
            Screen(
                backStackEntry.toRoute<T>(this::class),
                { route -> navigateTo(backStackEntry, route) },
            ) { navigateBack(backStackEntry) }
        }
    }

    context(navigationSuiteScope: NavigationSuiteScope)
    override fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: NavigationNode<T>.(String) -> String
    ): Unit = with(navigationSuiteScope) {
        val selected = isSelected(currentDestination)

        val label = transform(this@AbstractDestination.label)

        val navItem = NavigationItem(
            modifier,
            selectedModifier,
            enabled,
            alwaysShowLabel,
            { modifier -> Text(label, modifier) },
            { SelectedText(label, modifier) },
            { modifier -> Icon(label, modifier) },
            { modifier -> SelectedIcon(label, modifier) },
            { modifier -> Badge(label, modifier) },
            { modifier -> SelectedBadge(label, modifier) },
        )

        item(
            selected,
            {
                navController.navigate(this) {
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

    private fun isSelected(currentDestination: NavDestination?): Boolean =
        currentDestination?.hierarchy?.any { it.hasRoute(this::class) } == true
}
