package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.components.navigation.viewmodel.AbstractNavViewModel
import clib.presentation.components.navigation.Navigator
import org.koin.android.annotation.KoinViewModel
import ui.navigation.presentation.NavRoute

@KoinViewModel
public class NavViewModel(
    override val navigator: Navigator<NavRoute, *>,
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractNavViewModel<NavRoute>()
