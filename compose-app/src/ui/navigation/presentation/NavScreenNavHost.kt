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
import clib.ui.presentation.event.navigator.NavigationAction
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.profile.presentation.ProfileScreen
import ui.main.MainScreen
import ui.map.MapScreen
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
        composable<Destination.Main>(
            Destination.Main.typeMap,
            Destination.Main.deepLinks,
        ) { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

            MainScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        composable<Destination.Map>(
            Destination.Map.typeMap,
            Destination.Map.deepLinks,
        ) { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

            MapScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        composable<Destination.Settings>(
            Destination.Settings.typeMap,
            Destination.Settings.deepLinks,
        ) { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

            SettingsScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        composable<Destination.About>(
            Destination.About.typeMap,
            Destination.About.deepLinks,
        ) { backStackEntry ->
            val navViewModel =
                koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

            AboutScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                navViewModel.action(NavigationAction.NavigateBack)
            }
        }

        navigation<Destination.AuthGraph>(Destination.Login) {
            composable<Destination.Login>(
                Destination.Login.typeMap,
                Destination.Login.deepLinks,
            ) { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

                LoginScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            composable<Destination.ForgotPassword>(
                Destination.ForgotPassword.typeMap,
                Destination.ForgotPassword.deepLinks,
            ) { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

                ForgotPasswordScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            composable<Destination.Profile>(
                Destination.Profile.typeMap,
                Destination.Profile.deepLinks,
            ) { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

                ProfileScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }
        }

        navigation<Destination.WalletGraph>(Destination.Balance) {
            composable<Destination.Balance>(
                Destination.Balance.typeMap,
                Destination.Balance.deepLinks,
            ) { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

                BalanceScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            composable<Destination.Crypto>(
                Destination.Crypto.typeMap,
                Destination.Crypto.deepLinks,
            ) { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

                CryptoScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }

            composable<Destination.Stock>(
                Destination.Stock.typeMap,
                Destination.Stock.deepLinks,
            ) { backStackEntry ->
                val navViewModel =
                    koinViewModel<Destination.NavGraph, NavViewModel>(navController = navController, backStackEntry = backStackEntry)

                StockScreen({ destination -> navViewModel.action(NavigationAction.SafeNavigation.Navigate(destination)) }) {
                    navViewModel.action(NavigationAction.NavigateBack)
                }
            }
        }
    }
