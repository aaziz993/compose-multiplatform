package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.model.NavigationEndpoint
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import clib.presentation.event.navigator.Navigator
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class NavViewModel(
    navigator: Navigator<NavigationEndpoint>,
    savedStateHandle: SavedStateHandle
) : AbstractNavViewModel<NavigationEndpoint>(navigator, savedStateHandle)
