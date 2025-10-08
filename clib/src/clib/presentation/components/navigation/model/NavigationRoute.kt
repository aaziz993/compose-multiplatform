package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
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
import clib.presentation.components.navigation.viewmodel.NavigationAction
import klib.data.type.auth.AuthResource
import klib.data.type.auth.User
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlinx.serialization.serializer

@Immutable
public sealed interface Route {

    public val deepLinks: List<String>
    public val enabled: Boolean
    public val alwaysShowLabel: Boolean

    public fun authResource(): AuthResource? = AuthResource()

    public fun authProvider(): String? = null
    public fun authUser(): User? = null

    public fun auth(): Boolean = authResource()?.validate(authProvider(), authUser()) != false

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
        navigationAction: NavBackStackEntry.(NavigationAction) -> Unit,
    )

    public fun excludeFromNavigation(): Boolean = false

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        enabled: Boolean = true,
        alwaysShowLabel: Boolean = true,
        text: @Composable (label: String, modifier: Modifier) -> Unit = { label, modifier ->
            Text(label, modifier)
        },
        selectedText: @Composable (label: String, modifier: Modifier) -> Unit = text,
        currentDestination: NavDestination?,
        navigateTo: (NavigationDestination<*>) -> Unit
    )
}

@Suppress("UNCHECKED_CAST")
public abstract class NavigationDestination<Dest : Any> : Route {

    public open val kClass: KClass<Dest>
        get() = this::class as KClass<Dest>

    public val label: String
        get() = kClass.serializer().descriptor.serialName

    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    override val enabled: Boolean = true
    override val alwaysShowLabel: Boolean = true

    protected open val modifier: Modifier = Modifier.Companion
    protected open val selectedModifier: Modifier = modifier
    protected open val icon: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }
    protected open val selectedIcon: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }
    protected open val badge: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }
    protected open val selectedBadge: @Composable (label: String, modifier: Modifier) -> Unit = { _, _ -> }

    @Composable
    protected open fun Screen(
        route: Dest,
        navigationAction: (NavigationAction) -> Unit,
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
        navigationAction: NavBackStackEntry.(NavigationAction) -> Unit,
    ): Unit = with(navGraphBuilder) {
        if (!auth()) return@with

        val concatenatedDeepLinks = this@NavigationDestination.deepLinks.concatenateDeepLinks(deepLinks)

        composable(
            this@NavigationDestination.kClass,
            typeMap + this@NavigationDestination.typeMap,
            concatenatedDeepLinks.map { basePath ->
                navDeepLink(this@NavigationDestination.kClass, basePath) {}
            },
            enterTransition,
            exitTransition,
            popEnterTransition,
            popExitTransition,
            sizeTransform,
        ) { backStackEntry ->
            Screen(backStackEntry.toRoute(this@NavigationDestination.kClass)) { action ->
                backStackEntry.navigationAction(action)
            }
        }
    }

    context(navigationSuiteScope: NavigationSuiteScope)
    override fun item(
        enabled: Boolean,
        alwaysShowLabel: Boolean,
        text: @Composable (label: String, modifier: Modifier) -> Unit,
        selectedText: @Composable (label: String, modifier: Modifier) -> Unit,
        currentDestination: NavDestination?,
        navigateTo: (NavigationDestination<*>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (excludeFromNavigation() || !auth()) return@with

        val selected = isSelected(currentDestination)

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
            { navigateTo(this@NavigationDestination) },
            { selectedItem.icon?.invoke(Modifier) },
            selectedItem.modifier,
            enabled && this@NavigationDestination.enabled,
            {
                selectedItem.text?.invoke(Modifier)
            },
            alwaysShowLabel && this@NavigationDestination.alwaysShowLabel,
            { selectedItem.badge?.invoke(Modifier) },
        )
    }

    public fun isSelected(currentDestination: NavDestination?): Boolean =
        currentDestination?.hierarchy?.any { destination -> destination.hasRoute(kClass) } == true
}

public abstract class NavigationRoute : Route {

    override val enabled: Boolean = true
    override val alwaysShowLabel: Boolean = true

    public open val routes: List<Route> = emptyList()

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
        navigationAction: NavBackStackEntry.(NavigationAction) -> Unit,
    ): Unit = with(navGraphBuilder) {
        if (!auth()) return@with

        val concatenatedDeepLinks = this@NavigationRoute.deepLinks.concatenateDeepLinks(deepLinks)
        navigation(this@NavigationRoute::class, routes.first()) {
            routes.forEach { route ->
                route.item(
                    typeMap,
                    concatenatedDeepLinks,
                    enterTransition,
                    exitTransition,
                    popEnterTransition,
                    popExitTransition,
                    sizeTransform,
                    navigationAction,
                )
            }
        }
    }

    context(navigationSuiteScope: NavigationSuiteScope)
    override fun item(
        enabled: Boolean,
        alwaysShowLabel: Boolean,
        text: @Composable (label: String, modifier: Modifier) -> Unit,
        selectedText: @Composable (label: String, modifier: Modifier) -> Unit,
        currentDestination: NavDestination?,
        navigateTo: (NavigationDestination<*>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (excludeFromNavigation() || !auth()) return@with

        routes.forEach { route ->
            route.item(enabled, alwaysShowLabel, text, selectedText, currentDestination, navigateTo)
        }
    }

    public fun selected(currentDestination: NavDestination?): NavigationDestination<*>? =
        routes.filterNot { route ->
            route.excludeFromNavigation() || !route.auth()
        }.firstNotNullOfOrNull { route ->
            when (route) {
                is NavigationDestination<*> -> if (route.isSelected(currentDestination)) route else null
                is NavigationRoute -> route.selected(currentDestination)
            }
        }
}

private fun List<String>.concatenateDeepLinks(deepLinks: List<String>) =
    if (deepLinks.isEmpty()) this
    else deepLinks.flatMap { basePath0 -> map { basePath1 -> "$basePath0/$basePath1" } }

