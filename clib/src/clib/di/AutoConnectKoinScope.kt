package clib.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import org.koin.compose.ComposeContextWrapper
import org.koin.compose.LocalKoinScope
import org.koin.compose.getKoin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

@Composable
public fun AutoConnectKoinScope(
    navController: NavController,
    lastKnownNavGraphRoute: String? = null,
    rootScope: Scope = getKoin().scopeRegistry.rootScope,
    content: @Composable () -> Unit
) {
    val koin = getKoin()

    // Holds the currently injected Koin Scope.
    var scopeToInject by remember {
        mutableStateOf(
            value = if (lastKnownNavGraphRoute == null) rootScope
            else koin.getOrCreateScope(
                scopeId = lastKnownNavGraphRoute,
                qualifier = named(lastKnownNavGraphRoute),
            ),
        )
    }

    // Listener to navigation changes.
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val currentNavGraphRoute = destination.parent?.route
            val previousNavGraphRoute = lastKnownNavGraphRoute

            if (previousNavGraphRoute != null && currentNavGraphRoute != previousNavGraphRoute) {
                val lastScope = koin.getOrCreateScope(
                    scopeId = previousNavGraphRoute,
                    qualifier = named(previousNavGraphRoute),
                )
                lastScope.close()
            }

            if (currentNavGraphRoute == null) scopeToInject = rootScope
            else {
                val scopeForCurrentNavGraphRoute = koin.getOrCreateScope(
                    scopeId = currentNavGraphRoute,
                    qualifier = named(currentNavGraphRoute),
                )
                scopeToInject = scopeForCurrentNavGraphRoute
            }
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    CompositionLocalProvider(
        LocalKoinScope provides ComposeContextWrapper(scopeToInject),
        content = content,
    )
}
