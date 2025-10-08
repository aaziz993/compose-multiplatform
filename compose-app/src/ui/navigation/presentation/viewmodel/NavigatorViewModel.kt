package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.Navigator
import clib.presentation.components.navigation.viewmodel.AbstractNavigatorViewModel
import org.koin.android.annotation.KoinViewModel
import ui.navigation.presentation.Destination

@KoinViewModel
public class NavigatorViewModel(
    override val navigator: Navigator<Destination>,
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractNavigatorViewModel<Destination>()
