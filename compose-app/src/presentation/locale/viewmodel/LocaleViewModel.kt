package presentation.locale.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.locale.viewmodel.AbstractLocaleViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class LocaleViewModel(
    override val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : AbstractLocaleViewModel()
