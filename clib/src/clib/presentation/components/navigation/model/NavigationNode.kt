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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

@Immutable
public interface NavigationNode<T : NavigationNode<T>> {

    public val deepLinks: List<String>

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
        navigateTo: (NavBackStackEntry, route: AbstractDestination<T>) -> Unit = { _, _ -> },
        navigateBack: (NavBackStackEntry) -> Unit = {}
    )

    context(navigationSuiteScope: NavigationSuiteScope)
    public fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: NavigationNode<T>.(label: String) -> String = { it }
    )

    public fun deepDeepLinks(deepLinks: List<String>): List<String> =
        if (deepLinks.isEmpty()) this.deepLinks
        else deepLinks.flatMap { basePath0 -> this.deepLinks.map { basePath1 -> "$basePath0/$basePath1" } }
}
