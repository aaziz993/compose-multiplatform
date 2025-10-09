package ui.auth.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.auth.viewmodel.AbstractAuthViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class AuthViewModel(
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractAuthViewModel()
