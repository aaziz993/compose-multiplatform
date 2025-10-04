package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

@Immutable
public abstract class NavigationRoute<Route : NavigationRoute<Route, *>, Dest : Any> {

    public val label: String
        get() = this::class.simpleName!!
    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    public open val deepLinks: List<String> = emptyList()

    protected open val modifier: Modifier = Modifier.Companion
    protected open val selectedModifier: Modifier = modifier
    protected open val enabled: Boolean = true
    protected open val alwaysShowLabel: Boolean = true

    public open val composableChildren: List<NavigationRoute<Route, *>> = emptyList()
    public open val navigationChildren: List<NavigationRoute<Route, *>>
        get() = composableChildren

    @Composable
    protected open fun Text(label: String, modifier: Modifier = Modifier.Companion): Unit =
        androidx.compose.material3.Text(text = label)

    @Composable
    protected open fun SelectedText(label: String, modifier: Modifier = Modifier.Companion): Unit =
        androidx.compose.material3.Text(text = label)

    @Composable
    protected open fun Icon(label: String, modifier: Modifier = Modifier.Companion): Unit = Unit

    @Composable
    protected open fun SelectedIcon(label: String, modifier: Modifier = Modifier.Companion): Unit = Unit

    @Composable
    protected open fun Badge(label: String, modifier: Modifier = Modifier.Companion): Unit = Unit

    @Composable
    protected open fun SelectedBadge(label: String, modifier: Modifier = Modifier.Companion): Unit = Unit

    @Composable
    protected open fun Screen(
        route: Dest,
        navigateTo: (NavigationRoute<Route, *>) -> Unit,
        navigateBack: () -> Unit
    ): Unit = Unit

    context(navGraphBuilder: NavGraphBuilder)
    public fun item(
        typeMap: Map<KType, NavType<*>> = emptyMap(),
        deepLinks: List<String> = emptyList(),
        enterTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
            {
                fadeIn(animationSpec = tween(700))
            },
        exitTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
            {
                fadeOut(animationSpec = tween(700))
            },
        popEnterTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
            enterTransition,
        popExitTransition:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
            exitTransition,
        sizeTransform:
        (@JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)? =
            null,
        navigateTo: (NavBackStackEntry, route: NavigationRoute<Route, *>) -> Unit,
        navigateBack: (NavBackStackEntry) -> Unit
    ): Unit = with(navGraphBuilder) {
        val concatenatedTypeMap = concatenateTypeMap(typeMap)
        val concatenatedDeepLinks = concatenateDeepLinks(deepLinks)

        if (composableChildren.isNotEmpty())
            return@with navigation(this@NavigationRoute::class, composableChildren.first()) {
                composableChildren.forEach { child ->
                    child.item(
                        concatenatedTypeMap,
                        concatenatedDeepLinks,
                        enterTransition,
                        exitTransition,
                        popEnterTransition,
                        popExitTransition,
                        sizeTransform,
                        navigateTo,
                        navigateBack,
                    )
                }
            }

        composable(
            this@NavigationRoute::class,
            concatenatedTypeMap,
            concatenatedDeepLinks.map { basePath ->
                navDeepLink(this@NavigationRoute::class, basePath) {}
            },
            enterTransition,
            exitTransition,
            popEnterTransition,
            popExitTransition,
            sizeTransform,
        ) { backStackEntry ->
            Screen(
                backStackEntry.toRoute(this@NavigationRoute::class),
                { route -> navigateTo(backStackEntry, route) },
            ) { navigateBack(backStackEntry) }
        }
    }

    private fun concatenateTypeMap(typeMap: Map<KType, NavType<*>>) = typeMap + this.typeMap

    private fun concatenateDeepLinks(deepLinks: List<String>) =
        if (deepLinks.isEmpty()) this.deepLinks
        else deepLinks.flatMap { basePath0 -> this.deepLinks.map { basePath1 -> "$basePath0/$basePath1" } }

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: NavigationRoute<Route, *>.(String) -> String
    ): Unit = with(navigationSuiteScope) {
        if (navigationChildren.isNotEmpty())
            return@with navigationChildren.forEach { child ->
                child.item(navController, currentDestination, transform)
            }

        val selected = isSelected(currentDestination)

        val label = transform(this@NavigationRoute.label)

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
                navController.navigate(this@NavigationRoute) {
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
