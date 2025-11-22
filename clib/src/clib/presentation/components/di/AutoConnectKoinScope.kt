package clib.presentation.components.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import org.koin.compose.ComposeContextWrapper
import clib.di.LocalKoinScopeContext
import clib.di.getKoin
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Router
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

@Composable
public fun AutoConnectKoinScope(
    router: Router,
    rootScope: Scope = getKoin().scopeRegistry.rootScope,
    content: @Composable () -> Unit
) {
    val koin = getKoin()
    val scopeMap = remember { mutableStateMapOf<BaseRoute, Scope>() }

    val scopeToInject by remember {
        derivedStateOf {
            val currentRoute = router.backStack.lastOrNull()?.route ?: return@derivedStateOf rootScope

            // Close scopes no longer in backStack
            val activeRoutes = router.backStack.map(NavRoute::route)
            val closedScopes = scopeMap.keys - activeRoutes.toSet()
            closedScopes.forEach { route ->
                scopeMap.remove(route)?.close()
            }

            // Get or create scope for current route
            scopeMap.getOrPut(currentRoute) {
                koin.getOrCreateScope(
                    scopeId = currentRoute.name,
                    qualifier = named(currentRoute.name),
                )
            }
        }
    }

    CompositionLocalProvider(
        LocalKoinScopeContext provides ComposeContextWrapper(scopeToInject),
        content = content,
    )
}
