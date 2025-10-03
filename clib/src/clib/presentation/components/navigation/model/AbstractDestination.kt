package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentScope
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
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType
import kotlinx.serialization.Transient

@Immutable
public abstract class AbstractDestination {

    public val label: String
        get() = this::class.simpleName!!

    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    public open val deepLinks: List<NavDeepLink> = emptyList()

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

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: AbstractDestination.(label: String) -> String = { it }
    ) {
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

        navigationSuiteScope.item(
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

    context(navGraphBuilder: NavGraphBuilder)
    public fun composable(
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
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ): Unit = navGraphBuilder.composable(
        this::class,
        typeMap,
        deepLinks,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        sizeTransform,
        content,
    )

    private fun AbstractDestination.isSelected(currentDestination: NavDestination?) =
        currentDestination?.hierarchy?.any { it.hasRoute(this::class) } == true
}


