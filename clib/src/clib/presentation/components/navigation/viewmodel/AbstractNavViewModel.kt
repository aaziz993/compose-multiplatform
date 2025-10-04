package clib.presentation.components.navigation.viewmodel

import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.Navigator
import clib.presentation.viewmodel.AbstractViewModel

public abstract class AbstractNavViewModel<Route : NavigationRoute<Route, *>> : AbstractViewModel<NavigationAction>() {

    protected abstract val navigator: Navigator<Route, *>

    override fun action(action: NavigationAction) {
        navigator.navigate(action)
    }
}
