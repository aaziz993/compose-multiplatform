package ui.navigation.presentation

import ui.about.AboutScreen
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import clib.di.koinViewModel
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType
import clib.presentation.event.navigator.NavigationAction
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.profile.presentation.ProfileScreen
import ui.map.MapScreen
import ui.home.HomeScreen
import ui.navigation.presentation.viewmodel.NavViewModel
import ui.settings.SettingsScreen
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.stock.StockScreen

@Composable
public fun NavScreenNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    route: KClass<*>? = null,
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
): Unit =
    NavHost(
        navController,
        startDestination,
        modifier,
        contentAlignment,
        route,
        typeMap,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        sizeTransform,
    ) {
        Destination.Home.composable { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

            HomeScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        Destination.Map.composable { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

            MapScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        Destination.Settings.composable { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

            SettingsScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        Destination.About.composable { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

            AboutScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        navigation<Destination.AuthGraph>(Destination.Login) {
            Destination.Login.composable { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

                LoginScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            Destination.ForgotPassword.composable { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

                ForgotPasswordScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            Destination.Profile.composable { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

                ProfileScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }
        }

        navigation<Destination.WalletGraph>(Destination.Balance) {
            Destination.Balance.composable { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

                BalanceScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            Destination.Crypto.composable { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

                CryptoScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            Destination.Stock.composable {  backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController, backStackEntry)

                StockScreen({ destination -> navViewModel.action(NavigationAction.TypeSafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }
        }
    }
