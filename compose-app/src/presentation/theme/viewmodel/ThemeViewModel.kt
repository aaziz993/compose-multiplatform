package presentation.theme.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.theme.viewmodel.AbstractThemeViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class ThemeViewModel(
    override val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : AbstractThemeViewModel()
