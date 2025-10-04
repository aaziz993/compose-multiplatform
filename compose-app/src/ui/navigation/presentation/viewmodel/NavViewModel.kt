package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import org.koin.android.annotation.KoinViewModel
import clib.presentation.event.navigator.Navigator
import ui.navigation.presentation.Route

@KoinViewModel
public class NavViewModel(
    navigator: Navigator<Route>,
    savedStateHandle: SavedStateHandle
) : AbstractNavViewModel<Route>(navigator, savedStateHandle)
