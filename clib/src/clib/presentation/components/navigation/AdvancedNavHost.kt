package clib.presentation.components.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.NavigationEndpoint
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

@Composable
public fun AdvancedNavHost(
    navController: NavHostController,
    route: NavigationRoute,
    startDestination: NavigationDestination,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    typeMap: Map<KType, NavType<*>> = emptyMap(),
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
    builder: NavGraphBuilder.(NavigationEndpoint) -> Unit
): Unit =
    NavHost(
        navController,
        startDestination,
        modifier,
        contentAlignment,
        route::class,
        typeMap,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        sizeTransform,
    ) {
        route.composableChildren.forEach { composableChild ->
            builder(composableChild)
        }
    }
