package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import clib.presentation.auth.LocalAuth
import clib.presentation.components.model.item.Item
import clib.presentation.components.navigation.viewmodel.NavigationAction
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth
import klib.data.type.collections.iterator
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
@Immutable
public sealed class Route<out Dest : Any> {

    protected open val route: KClass<out Dest>
        get() = this::class as KClass<out Dest>

    public open val label: String
        get() = route.serializer().descriptor.serialName

    protected open val deepLinks: List<String>
        get() = listOf(label)

    protected open val enabled: Boolean
        get() = true
    protected open val alwaysShowLabel: Boolean
        get() = true

    protected open fun authResource(): AuthResource? = null

    protected fun auth(auth: Auth): Boolean =
        authResource()?.validate(auth.provider, auth.user) != false

    context(navGraphBuilder: NavGraphBuilder)
    public abstract fun item(
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

    public open fun isNavigateItem(): Boolean = true

    protected open val modifier: Modifier
        get() = Modifier
    protected open val selectedModifier: Modifier
        get() = modifier
    protected open val text: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { text, modifier -> Text(text = text, modifier = modifier) }
    protected open val selectedText: @Composable (label: String, modifier: Modifier) -> Unit
        get() = text
    protected open val icon: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    protected open val selectedIcon: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    protected open val badge: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    protected open val selectedBadge: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }

    @Composable
    public open fun ScreenAppBar(block: @Composable (innerPadding: PaddingValues) -> Unit): Unit =
        block(PaddingValues(0.dp))

    context(navigationSuiteScope: NavigationSuiteScope)
    public open fun item(
        auth: Auth = Auth(),
        enabled: Boolean = true,
        alwaysShowLabel: Boolean = true,
        transform: @Composable (label: String) -> String = { it.uppercaseFirstChar() },
        destination: NavDestination?,
        navigateTo: (Route<Dest>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (!canNavigateItem(auth)) return@with

        val selected = hasDestination(destination)

        val selectedItem = if (selected)
            Item(
                selectedModifier,
                { selectedText(transform(label), it) },
                { selectedIcon(transform(label), it) },
                { selectedBadge(transform(label), it) },
            )
        else Item(
            modifier,
            { text(transform(label), it) },
            { icon(transform(label), it) },
            { badge(transform(label), it) },
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

    public fun canNavigateItem(auth: Auth = Auth()): Boolean = isNavigateItem() && auth(auth)

    public fun hasDestination(destination: NavDestination?): Boolean =
        destination?.hierarchy?.any { destination -> destination.hasRoute(route) } == true

    public fun isDestination(destination: NavDestination?): Boolean =
        destination?.hasRoute(route) == true
}

@Suppress("UNCHECKED_CAST")
public abstract class NavigationDestination<Dest : Any> : Route<Dest>() {

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

public abstract class NavigationRoute<Dest : Any> : Route<Dest>(), Sequence<Route<Dest>> {

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
        transform: @Composable (label: String) -> String,
        destination: NavDestination?,
        navigateTo: (Route<Dest>) -> Unit
    ): Unit = with(navigationSuiteScope) {
        if (!canNavigateItem(auth)) return@with

        if (expand)
            routes.forEach { route ->
                route.item(auth, enabled, alwaysShowLabel, transform, destination, navigateTo)
            }
        else super.item(auth, enabled, alwaysShowLabel, transform, destination, navigateTo)
    }

    override fun iterator(): Iterator<Route<Dest>> = sequence {
        val (childRoutes, childDestinations) = routes.partition { route -> route is NavigationRoute }
        yieldAll(childDestinations)

        childRoutes.forEach { route ->
            yieldAll((route as NavigationRoute<Dest>).iterator())
        }
    }.iterator()
}

private fun List<String>.concatenateDeepLinks(deepLinks: List<String>) =
    if (deepLinks.isEmpty()) this
    else deepLinks.flatMap { basePath0 -> map { basePath1 -> "$basePath0/$basePath1" } }

