package clib.di.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Route
import clib.presentation.navigation.Routes
import org.koin.core.component.KoinComponent

public abstract class KoinRoute<T : NavRoute> : Route<T>(), KoinComponent

public abstract class KoinRoutes : Routes(), KoinComponent {

    @Composable
    override fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>,
    ): Unit = androidx.navigation3.ui.NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberKoinScopeNavEntryDecorator(),
        ),
        entryProvider = entryProvider,
    )
}




