package clib.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.viewmodel.defaultExtras
import org.koin.viewmodel.resolveViewModel

/*
    Ported directly from Android side. Waiting more feedback
 */

/**
 * Resolve ViewModel instance
 *
 * @param qualifier
 * @param parameters
 *
 * @author Arnaud Giuliani
 */

@OptIn(KoinInternalApi::class)
@Composable
public inline fun <reified T : ViewModel> koinViewModel(
    qualifier: Qualifier? = null,
    viewModelStoreOwner: ViewModelStoreOwner = LocalViewModelStoreOwner.current
        ?: error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"),
    key: String? = null,
    extras: CreationExtras = defaultExtras(viewModelStoreOwner),
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    return resolveViewModel(
        T::class, viewModelStoreOwner.viewModelStore, key, extras, qualifier, scope, parameters,
    )
}
