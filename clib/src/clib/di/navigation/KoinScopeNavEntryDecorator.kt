package clib.di.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntryDecorator
import clib.di.LocalKoinScopeContext
import clib.di.getKoin
import clib.presentation.navigation.Routes
import org.koin.compose.ComposeContextWrapper
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import org.koin.core.scope.Scope

public class KoinScopeNavEntryDecorator<T : Any>(
    private val scope: Scope,
) : NavEntryDecorator<T>(
    decorate = { entry ->
        CompositionLocalProvider(
            LocalKoinScopeContext provides ComposeContextWrapper(scope),
        ) {
            entry.Content()
        }
    },
)

context(routes: Routes)
@Composable
public fun <T : Any> rememberKoinScopeNavEntryDecorator(): KoinScopeNavEntryDecorator<T> {
    val koin = getKoin()
    val scope = remember { koin.getOrCreateScope(routes.getScopeId(), routes.getScopeName(), routes) }

    DisposableEffect(Unit) {
        onDispose(scope::close)
    }

    return remember { KoinScopeNavEntryDecorator(scope) }
}
