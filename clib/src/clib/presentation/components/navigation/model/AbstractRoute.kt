package clib.presentation.components.navigation.model

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navigation
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

public abstract class AbstractRoute<T : NavigationNode<T>> : NavigationNode<T> {

    public abstract val composableChildren: List<T>

    public open val navigationChildren: List<T> = composableChildren

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
        viewModel: @Composable (NavBackStackEntry) -> AbstractNavViewModel<T>
    ): Unit = with(navGraphBuilder) {
        val deepDeepLinks = deepDeepLinks(deepLinks)
        navigation(this::class, composableChildren.first()) {
            composableChildren.forEach { child ->
                child.item(
                    typeMap,
                    deepDeepLinks,
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

    context(navigationSuiteScope: NavigationSuiteScope)
    override fun item(
        navController: NavController,
        currentDestination: NavDestination?,
        transform: NavigationNode<T>.(String) -> String
    ): Unit = navigationChildren.forEach { child ->
        child.item(navController, currentDestination, transform)
    }
}



