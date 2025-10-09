package ui.auth.otp.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.AbstractViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class OtpViewModel : AbstractViewModel<OtpAction>() {

    public val state: MutableStateFlow<OtpState>
        field = viewModelMutableStateFlow(OtpState())

    override fun action(action: OtpAction): Unit = when (action) {
        is OtpAction.SetCode -> setCode(action.value)
        is OtpAction.SendCode -> {}
        is OtpAction.Confirm -> confirm()
    }

    private fun setCode(value: String) =
        state.update { it.copy(code = value, error = null) }

    private fun confirm() {
        viewModelScope.launch {
            if (state.value.code == "1234") state.update { it.copy(confirmed = true) }
            else state.update { it.copy(error = "Invalid code") }
        }
    }
}
