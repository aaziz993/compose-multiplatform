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
import klib.data.type.auth.model.Auth
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
@Immutable
public sealed interface Route<out Dest : Any> {

    public val route: KClass<out Dest>
        get() = this::class as KClass<out Dest>

    public val label: String
        get() = route.serializer().descriptor.serialName

    public val deepLinks: List<String>
        get() = listOf(label)

    public val enabled: Boolean
        get() = true
    public val alwaysShowLabel: Boolean
        get() = true

    public fun authResource(): AuthResource? = null

    public fun auth(auth: Auth): Boolean = authResource()?.validate(auth.provider, auth.user) != false

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
        onNavigationAction: NavBackStackEntry.(NavigationAction) -> Unit,
    )

    public fun isNavigateItem(): Boolean = true

    public val modifier: Modifier
        get() = Modifier
    public val selectedModifier: Modifier
        get() = modifier
    public val icon: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    public val selectedIcon: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    public val badge: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    public val selectedBadge: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        auth: Auth = Auth(),
        enabled: Boolean = true,
        alwaysShowLabel: Boolean = true,
        text: @Composable (label: String, modifier: Modifier) -> Unit,
        selectedText: @Composable (label: String, modifier: Modifier) -> Unit = text,
        destination: NavDestination?,
        navigateTo: (Route<Dest>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (!isNavigateItem() || !auth(auth)) return@with

        val selected = isSelected(destination)

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
            { navigateTo(this@Route) },
            { selectedItem.icon?.invoke(Modifier) },
            selectedItem.modifier,
            enabled && this@Route.enabled,
            {
                selectedItem.text?.invoke(Modifier)
            },
            alwaysShowLabel && this@Route.alwaysShowLabel,
            { selectedItem.badge?.invoke(Modifier) },
        )
    }

    public fun isSelected(destination: NavDestination?): Boolean =
        destination?.hierarchy?.any { destination -> destination.hasRoute(route) } == true

    public fun isDestination(destination: NavDestination?): Boolean =
        destination?.hasRoute(route) == true
}

@Suppress("UNCHECKED_CAST")
public abstract class NavigationDestination<Dest : Any> : Route<Dest> {

    public open val typeMap: Map<KType, NavType<*>> = emptyMap()

    @Composable
    protected open fun Screen(
        route: Dest,
        onNavigationAction: (NavigationAction) -> Unit,
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
        onNavigationAction: NavBackStackEntry.(NavigationAction) -> Unit,
    ): Unit = with(navGraphBuilder) {
        val concatenatedDeepLinks = this@NavigationDestination.deepLinks.concatenateDeepLinks(deepLinks)

        composable(
            this@NavigationDestination.route,
            typeMap + this@NavigationDestination.typeMap,
            concatenatedDeepLinks.map { basePath ->
                navDeepLink(this@NavigationDestination.route, basePath) {}
            },
            enterTransition,
            exitTransition,
            popEnterTransition,
            popExitTransition,
            sizeTransform,
        ) { backStackEntry ->
            Screen(backStackEntry.toRoute(this@NavigationDestination.route)) { action ->
                backStackEntry.onNavigationAction(action)
            }
        }
    }
}

public abstract class NavigationRoute<Dest : Any> : Route<Dest> {

    public open val expand: Boolean = false

    public open val routes: List<Route<Dest>> = emptyList()

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
        onNavigationAction: NavBackStackEntry.(NavigationAction) -> Unit,
    ): Unit = with(navGraphBuilder) {
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
                    onNavigationAction,
                )
            }
        }
    }

    context(navigationSuiteScope: NavigationSuiteScope)
    override fun item(
        auth: Auth,
        enabled: Boolean,
        alwaysShowLabel: Boolean,
        text: @Composable (label: String, modifier: Modifier) -> Unit,
        selectedText: @Composable (label: String, modifier: Modifier) -> Unit,
        destination: NavDestination?,
        navigateTo: (Route<Dest>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (!isNavigateItem() || !auth(auth)) return@with

        if (expand)
            routes.forEach { route ->
                route.item(auth, enabled, alwaysShowLabel, text, selectedText, destination, navigateTo)
            }
        else super.item(auth, enabled, alwaysShowLabel, text, selectedText, destination, navigateTo)
    }

    public fun find(destination: NavDestination?): Route<Dest>? =
        routes.firstNotNullOfOrNull { route ->
            if (route.isDestination(destination)) route else (route as? NavigationRoute)?.find(destination)
        }

    public fun findByLabel(label: String): Route<Dest>? =
        routes.firstNotNullOfOrNull { route ->
            if (route.label == label) route else (route as? NavigationRoute)?.findByLabel(label)
        }
}

private fun List<String>.concatenateDeepLinks(deepLinks: List<String>) =
    if (deepLinks.isEmpty()) this
    else deepLinks.flatMap { basePath0 -> map { basePath1 -> "$basePath0/$basePath1" } }

