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
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import clib.presentation.event.navigator.NavigationAction
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType
import kotlinx.serialization.Transient

public abstract class NavigationDestination : NavigationEndpoint() {

    @Transient
    public val label: String
        get() = this::class.simpleName!!

    @Transient
    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    @Transient
    protected open val modifier: Modifier = Modifier

    @Transient
    protected open val selectedModifier: Modifier = modifier

    @Transient
    protected open val enabled: Boolean = true

    @Transient
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
    protected open fun Screen(
        navigateTo: (route: NavigationRoute) -> Unit = {},
        navigateBack: () -> Unit = {}
    ): Unit = Unit

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
        viewModel: @Composable (NavBackStackEntry) -> AbstractNavViewModel<out NavigationRoute>
    ): Unit = with(navGraphBuilder) {
        val deepDeepLinks = deepDeepLinks(deepLinks)
        composable(
            this::class,
            typeMap + this@NavigationDestination.typeMap,
            deepDeepLinks.map { basePath ->
                navDeepLink(this::class, basePath) {}
            },
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

    context(navigationSuiteScope: NavigationSuiteScope)
    override fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: NavigationEndpoint.(String) -> String
    ): Unit = with(navigationSuiteScope) {
        val selected = isSelected(currentDestination)

        val label = transform(this@NavigationDestination.label)

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
}
