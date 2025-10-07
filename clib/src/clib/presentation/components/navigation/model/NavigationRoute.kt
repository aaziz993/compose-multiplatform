package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import clib.presentation.components.model.item.Item
import clib.presentation.components.model.item.SelectableItem
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType
import kotlinx.serialization.serializer

@Immutable
public abstract class NavigationRoute<Route : NavigationRoute<Route, *>, Dest : Any> {

    public val label: String
        get() = this::class.serializer().descriptor.serialName

    public open val typeMap: Map<KType, NavType<*>> = emptyMap()
    public open val deepLinks: List<String> = emptyList()
    protected open val enabled: Boolean = true
    protected open val alwaysShowLabel: Boolean = true

    protected open val modifier: Modifier = Modifier.Companion
    protected open val selectedModifier: Modifier = modifier
    protected open val text: @Composable (label: String, modifier: Modifier) -> Unit = { label, modifier ->
        androidx.compose.material3.Text(label, modifier)
    }
    protected open val selectedText: @Composable (label: String, modifier: Modifier) -> Unit = { label, modifier ->
        androidx.compose.material3.Text(label, modifier)
    }
    protected open val icon: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }
    protected open val selectedIcon: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }
    protected open val badge: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }
    protected open val selectedBadge: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }

    public open val composableChildren: List<NavigationRoute<Route, *>> = emptyList()
    public open val navigationChildren: List<NavigationRoute<Route, *>>
        get() = composableChildren

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

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: NavigationRoute<Route, *>.(String) -> String,
        navigateTo: (NavigationRoute<Route, *>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (navigationChildren.isNotEmpty())
            return@with navigationChildren.forEach { child ->
                child.item(navController, currentDestination, transform, navigateTo)
            }

        val selected = isSelected(currentDestination)

        val label = transform(this@NavigationRoute.label)

        val selectedItem = if (selected)
            Item(
                selectedModifier,
                { selectedText(label, it) },
                { selectedIcon(label, it) },
                { selectedBadge(label, it) },
            )
        else Item(
            modifier,
            { text(label, it) },
            { icon(label, it) },
            { badge(label, it) },
        )

        item(
            selected,
            { navigateTo(this@NavigationRoute) },
            { selectedItem.icon?.invoke(Modifier) },
            selectedItem.modifier,
            enabled,
            {
                selectedItem.text?.invoke(Modifier)
            },
            alwaysShowLabel,
            { selectedItem.badge?.invoke(Modifier) },
        )
    }

    public fun selected(currentDestination: NavDestination): NavigationRoute<Route, *>? =
        if (isSelected(currentDestination)) this
        else composableChildren.firstNotNullOfOrNull { child -> child.selected(currentDestination) }

    private fun concatenateTypeMap(typeMap: Map<KType, NavType<*>>) = typeMap + this.typeMap

    private fun concatenateDeepLinks(deepLinks: List<String>) =
        if (deepLinks.isEmpty()) this.deepLinks
        else deepLinks.flatMap { basePath0 -> this.deepLinks.map { basePath1 -> "$basePath0/$basePath1" } }

    private fun isSelected(currentDestination: NavDestination?): Boolean =
        currentDestination?.hierarchy?.any { destination -> destination.hasRoute(this::class) } == true
}
