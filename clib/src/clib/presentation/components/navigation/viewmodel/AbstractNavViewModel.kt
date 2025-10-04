package clib.presentation.components.navigation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.event.navigator.NavigationAction
import clib.presentation.event.navigator.Navigator
import clib.presentation.viewmodel.AbstractViewModel

public abstract class AbstractNavViewModel<T : NavigationRoute>(
    private val navigator: Navigator<T>,
    savedStateHandle: SavedStateHandle
) : AbstractViewModel<NavigationAction>(savedStateHandle) {

    override fun action(action: NavigationAction) {
        navigator.navigate(action)
    }
}
