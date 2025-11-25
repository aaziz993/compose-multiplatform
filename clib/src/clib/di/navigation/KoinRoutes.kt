package clib.di.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import clib.di.LocalKoinScopeContext
import clib.presentation.navigation.Navigator
import clib.presentation.navigation.Router
import clib.presentation.navigation.Routes
import org.koin.compose.ComposeContextWrapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import org.koin.core.module.Module

public abstract class KoinRoutes : Routes(), KoinComponent {

    @Composable
    final override fun Nav3Host(
        routerFactory: @Composable (Routes) -> Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
    ) {
        val scope = remember { getKoin().createScope(getScopeId(), getScopeName(), this) }

        DisposableEffect(scope) {
            onDispose { scope.close() }
        }

        CompositionLocalProvider(
            LocalKoinScopeContext provides ComposeContextWrapper(scope),
        ) {
            super.Nav3Host(routerFactory, navigatorFactory)
        }
    }

    context(module: Module)
    public fun registerScopedRouters() {
        filterIsInstance<Routes>().forEach { routes ->
            module.scope(getScopeName()) {
                scoped { Router(routes) }
            }
        }
    }
}
