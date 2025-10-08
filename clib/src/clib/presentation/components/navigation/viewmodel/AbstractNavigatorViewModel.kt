package clib.presentation.components.navigation.viewmodel

import clib.presentation.components.navigation.Navigator
import clib.presentation.viewmodel.AbstractViewModel

public abstract class AbstractNavigatorViewModel<Dest : Any> : AbstractViewModel<NavigationAction>() {

    protected abstract val navigator: Navigator<Dest>

    override fun action(action: NavigationAction) {
        navigator.navigate(action)
    }
}
