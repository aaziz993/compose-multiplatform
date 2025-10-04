package clib.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlin.reflect.KClass
import org.koin.compose.currentKoinScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.viewmodel.defaultExtras

/**
 * [parentBackStackEntry] is the route parentBackStackEntry to which the [ViewModel] will be scoped to. It is done so by getting the backStackEntry
 * from the backstack by looking up its route.
 */
@Composable
public inline fun <reified V : ViewModel> koinViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    parentBackStackEntry: NavBackStackEntry,
    qualifier: Qualifier? = null,
    key: String? = null,
    extras: CreationExtras? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): V {
    val parentEntry = remember(backStackEntry) {
        parentBackStackEntry
    }
    return koinViewModel(
        qualifier,
        parentEntry,
        key,
        extras ?: defaultExtras(parentEntry),
        scope,
        parameters,
    )
}

/**
 * [R] is the route to which the [ViewModel] will be scoped to. It is done so by getting the backStackEntry
 * from the backstack by looking up its route.
 */
@Composable
public inline fun <reified R : Any, reified V : ViewModel> koinViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    qualifier: Qualifier? = null,
    key: String? = null,
    extras: CreationExtras? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): V = koinViewModel(
    navController,
    backStackEntry,
    navController.getBackStackEntry<R>(),
    qualifier,
    key,
    extras,
    scope,
    parameters,
)

/**
 * [kClass] is the route class to which the [ViewModel] will be scoped to. It is done so by getting the backStackEntry
 * from the backstack by looking up its route.
 */
@Composable
public inline fun <R : Any, reified V : ViewModel> koinViewModel(
    kClass: KClass<R>,
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    qualifier: Qualifier? = null,
    key: String? = null,
    extras: CreationExtras? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): V = koinViewModel(
    navController,
    backStackEntry,
    navController.getBackStackEntry(kClass),
    qualifier,
    key,
    extras,
    scope,
    parameters,
)

/**
 * [route] is the route to which the [ViewModel] will be scoped to. It is done so by getting the backStackEntry
 * from the backstack by looking up its route.
 */
@Composable
public inline fun <R : Any, reified V : ViewModel> koinViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    route: R,
    qualifier: Qualifier? = null,
    key: String? = null,
    extras: CreationExtras? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): V = koinViewModel(
    navController,
    backStackEntry,
    navController.getBackStackEntry(route),
    qualifier,
    key,
    extras,
    scope,
    parameters,
)

/**
 * [route] is the route to which the [ViewModel] will be scoped to. It is done so by getting the backStackEntry
 * from the backstack by looking up its route.
 */
@Composable
public inline fun <reified V : ViewModel> koinViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    route: String,
    qualifier: Qualifier? = null,
    key: String? = null,
    extras: CreationExtras? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): V = koinViewModel(
    navController,
    backStackEntry,
    navController.getBackStackEntry(route),
    qualifier,
    key,
    extras,
    scope,
    parameters,
)
