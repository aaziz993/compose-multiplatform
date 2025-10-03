package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
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
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import clib.presentation.event.navigator.NavigationAction
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType
import kotlinx.serialization.Transient

@Immutable
public abstract class AbstractDestination {

    @Transient
    public val label: String
        get() = this::class.simpleName!!

    @Transient
    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    @Transient
    public open val deepLinks: List<String> = emptyList()

    @Transient
    protected open val modifier: Modifier = Modifier

    @Transient
    protected open val selectedModifier: Modifier = modifier

    @Transient
    protected open val enabled: Boolean = true

    @Transient
    protected open val alwaysShowLabel: Boolean = true

    @Transient
    protected open val children: List<AbstractDestination> = emptyList()

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

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: AbstractDestination.(label: String) -> String = { it }
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

    @Composable
    protected open fun Screen(
        navigateTo: (route: AbstractDestination) -> Unit = {},
        navigateBack: () -> Unit = {}
    ): Unit = Unit

    context(navGraphBuilder: NavGraphBuilder)
    public fun item(
        typeMap: Map<KType, NavType<*>> = emptyMap(),
        deepLinks: List<String> = emptyList(),
        enterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
        EnterTransition?)? =
            null,
        exitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
        ExitTransition?)? =
            null,
        popEnterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
        EnterTransition?)? =
            enterTransition,
        popExitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
        ExitTransition?)? =
            exitTransition,
        sizeTransform:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
        SizeTransform?)? =
            null,
        viewModel: @Composable (NavBackStackEntry) -> AbstractNavViewModel<out AbstractDestination>
    ): Unit = if (children.isEmpty())
        composable(deepLinks, enterTransition, exitTransition, popEnterTransition, popExitTransition, sizeTransform, viewModel)
    else navigation(enterTransition, exitTransition, popEnterTransition, popExitTransition, sizeTransform, viewModel)

    private fun AbstractDestination.isSelected(currentDestination: NavDestination?) =
        currentDestination?.hierarchy?.any { it.hasRoute(this::class) } == true

    context(navGraphBuilder: NavGraphBuilder)
    private fun composable(
        deepLinks: List<String>,
        enterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?,
        viewModel: @Composable (NavBackStackEntry) -> AbstractNavViewModel<out AbstractDestination>
    ): Unit = with(navGraphBuilder) {
        composable(
            this::class,
            typeMap + this@AbstractDestination.typeMap,
            deepLinks.map { basePath -> navDeepLink(this::class, basePath) {} },
            enterTransition,
            exitTransition,
            popEnterTransition,
            popExitTransition,
            sizeTransform,
        ) { backStackEntry ->
            val navViewModel = viewModel(backStackEntry)
            Screen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }
    }

    context(navGraphBuilder: NavGraphBuilder)
    private fun navigation(
        enterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?,
        viewModel: @Composable (NavBackStackEntry) -> AbstractNavViewModel<out AbstractDestination>
    ): Unit = with(navGraphBuilder) {
        navigation(this::class, children.first()) {
            children.forEach { children ->
                item(
                    typeMap,
                    deepLinks.flatMap { basePath ->
                        children.deepLinks.map { childrenBasePath ->
                            "$basePath/$childrenBasePath"
                        }
                    },
                    enterTransition,
                    exitTransition,
                    popEnterTransition,
                    popExitTransition,
                    sizeTransform,
                    viewModel,
                )
            }
        }
    }
}


