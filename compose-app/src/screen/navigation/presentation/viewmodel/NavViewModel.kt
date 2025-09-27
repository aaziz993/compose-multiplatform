package screen.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import org.koin.android.annotation.KoinViewModel
import presentation.event.navigator.NavigationAction
import presentation.event.navigator.Navigator
import presentation.viewmodel.AbstractViewModel
import screen.navigation.presentation.Destination

@KoinViewModel
public class NavViewModel(
    public val navigator: Navigator<Destination>,
    savedStateHandle: SavedStateHandle
) : AbstractViewModel<NavigationAction>(savedStateHandle) {

    override fun action(action: NavigationAction) {
        navigator.navigate(action)
    }
}
