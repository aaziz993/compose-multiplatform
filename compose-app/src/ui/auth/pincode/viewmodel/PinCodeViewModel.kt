package ui.auth.pincode.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class PinCodeViewModel : ViewModel<PinCodeAction>() {

    public val state: RestartableStateFlow<PinCodeState>
        field = MutableStateFlow(PinCodeState()).onStartStateIn { it }

    override fun action(action: PinCodeAction) {
        when (action) {
            is PinCodeAction.SetPinCode -> setPinCode(action.value)
            is PinCodeAction.RepeatPinCode -> repeatPinCode(action.value)
            is PinCodeAction.Confirm -> confirm()
        }
    }

    private fun setPinCode(value: String) = state.update { it.copy(pinCode = value) }

    private fun repeatPinCode(value: String) = state.update { it.copy(repeatPinCode = value) }

    private fun confirm() = viewModelScope.launch {

    }
}
