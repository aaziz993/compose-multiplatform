package clib.presentation.components.navigation.viewmodel

import clib.presentation.components.navigation.NavigationRoute
import clib.presentation.event.navigator.NavigationAction
import clib.presentation.event.navigator.Navigator
import clib.presentation.viewmodel.AbstractViewModel

public abstract class AbstractNavViewModel<Route : NavigationRoute<Route, *>> : AbstractViewModel<NavigationAction>() {

    protected abstract val navigator: Navigator<Route, *>

    override fun action(action: NavigationAction) {
        navigator.navigate(action)
    }
}
