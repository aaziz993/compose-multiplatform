package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import org.koin.android.annotation.KoinViewModel
import clib.presentation.event.navigator.NavigationAction
import clib.presentation.event.navigator.Navigator
import clib.presentation.viewmodel.AbstractViewModel
import ui.navigation.presentation.Destination

@KoinViewModel
public class NavViewModel(
    navigator: Navigator<Destination>,
    savedStateHandle: SavedStateHandle
) : AbstractNavViewModel<Destination>(navigator, savedStateHandle)
