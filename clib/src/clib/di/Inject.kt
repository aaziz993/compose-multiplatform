package clib.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * Resolve Koin dependency for given Type T
 *
 * <u>Note</u> this version unwrap parameters to ParametersHolder in order to let remember all parameters
 * This parameters unwrap will be triggered on recomposition
 *
 * For better performances we advise to use koinInject(Qualifier,Scope,ParametersHolder)
 *
 * @param qualifier - dependency qualifier
 * @param scope - Koin's root by default
 * @param parameters - injected parameters (with lambda & parametersOf())
 * @return instance of type T
 *
 * @author Arnaud Giuliani
 */
@Composable
@OptIn(KoinInternalApi::class)
public inline fun <reified T> koinInject(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition,
): T {
    val p = parameters.invoke()
    return remember(qualifier, scope, p) {
        scope.getWithParameters(T::class, qualifier, p)
    }
}

/**
 * Resolve Koin dependency for given Type T
 *
 * @param qualifier - dependency qualifier
 * @param scope - Koin's root by default
 * @param parametersHolder - parameters (used with parametersOf(), no lambda)
 * @return instance of type T
 *
 * @author Arnaud Giuliani
 */
@Composable
@OptIn(KoinInternalApi::class)
public inline fun <reified T> koinInject(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    parametersHolder: ParametersHolder,
): T {
    return remember(qualifier, scope, parametersHolder) {
        scope.getWithParameters(T::class, qualifier, parametersHolder)
    }
}

/**
 * Resolve Koin dependency for given Type T
 *
 * @param qualifier - dependency qualifier
 * @param scope - Koin's root by default
 * @return instance of type T
 *
 * @author Arnaud Giuliani
 */
@Composable
public inline fun <reified T> koinInject(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope()
): T {
    return remember(qualifier, scope) {
        scope.get(T::class, qualifier)
    }
}
