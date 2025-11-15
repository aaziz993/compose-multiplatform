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
import clib.presentation.navigation.NavRoute
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

@Composable
public fun AutoConnectKoinScope(
    backStack: List<NavRoute>,
    rootScope: Scope = getKoin().scopeRegistry.rootScope,
    content: @Composable () -> Unit
) {
    val koin = getKoin()
    val scopeMap = remember { mutableStateMapOf<String, Scope>() }

    val scopeToInject by remember {
        derivedStateOf {
            val currentRoute = backStack.lastOrNull()?.toString() ?: return@derivedStateOf rootScope

            // Close scopes no longer in backStack
            val activeRoutes = backStack.map(NavRoute::toString)
            val closedScopes = scopeMap.keys - activeRoutes.toSet()
            closedScopes.forEach { route ->
                scopeMap.remove(route)?.close()
            }

            // Get or create scope for current route
            scopeMap.getOrPut(currentRoute) {
                koin.getOrCreateScope(
                    scopeId = currentRoute,
                    qualifier = named(currentRoute),
                )
            }
        }
    }

    CompositionLocalProvider(
        LocalKoinScopeContext provides ComposeContextWrapper(scopeToInject),
        content = content,
    )
}
