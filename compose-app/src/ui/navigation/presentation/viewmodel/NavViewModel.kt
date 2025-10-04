package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import clib.presentation.event.navigator.Navigator
import org.koin.android.annotation.KoinViewModel
import ui.navigation.presentation.Node

@KoinViewModel
public class NavViewModel(
    navigator: Navigator<Node>,
    savedStateHandle: SavedStateHandle
) : AbstractNavViewModel<Node>(navigator, savedStateHandle)
