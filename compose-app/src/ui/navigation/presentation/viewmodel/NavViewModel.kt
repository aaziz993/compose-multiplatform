package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import org.koin.android.annotation.KoinViewModel
import clib.ui.presentation.event.navigator.NavigationAction
import clib.ui.presentation.event.navigator.Navigator
import clib.ui.presentation.viewmodel.AbstractViewModel
import ui.navigation.presentation.Destination

@KoinViewModel
public class NavViewModel(
    public val navigator: Navigator<Destination>,
    savedStateHandle: SavedStateHandle
) : AbstractViewModel<NavigationAction>(savedStateHandle) {

    override fun action(action: NavigationAction) {
        navigator.navigate(action)
    }
}
