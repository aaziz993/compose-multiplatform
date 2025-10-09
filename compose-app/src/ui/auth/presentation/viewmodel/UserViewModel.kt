package ui.auth.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.auth.viewmodel.AbstractUserViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class UserViewModel(
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractUserViewModel()
