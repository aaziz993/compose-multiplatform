package ui.auth.phone.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.navigation.Router
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.otp.model.HotpConfig
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import klib.data.location.Phone
import klib.data.type.collections.restartableflow.RestartableStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import ui.navigation.presentation.Hotp
import ui.navigation.presentation.Totp

@KoinViewModel
public class PhoneViewModel(
    @Provided
    private val otpConfig: OtpConfig,
    @Provided
    private val phone: ui.navigation.presentation.Phone,
    @Provided
    private val router: Router,
) : ViewModel<PhoneAction>() {

    public val state: RestartableStateFlow<PhoneState>
        field = MutableStateFlow(PhoneState()).onStartStateIn { it }

    override fun action(action: PhoneAction): Unit = when (action) {
        is PhoneAction.SetPhone -> setPhone(action.dial, action.number, action.isValid)
        PhoneAction.Confirm -> confirm()
    }

    private fun setPhone(dial: String, number: String, isValid: Boolean) =
        state.update { it.copy(phone = Phone(dial, number), isValid = isValid) }

    private fun confirm() {
        viewModelScope.launch(Dispatchers.Main) {
            if (state.value.isValid)
                router.push(
                    when (otpConfig) {
                        is HotpConfig -> Hotp(phone.username, state.value.phone.toString())
                        is TotpConfig -> Totp(phone.username, state.value.phone.toString())
                    },
                )
        }
    }
}
