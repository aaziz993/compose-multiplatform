package ui.auth.phone.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.navigation.Router
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.otp.model.HotpConfig
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
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
    private val router: Router,
    @Provided
    private val otpConfig: OtpConfig,
) : ViewModel<PhoneAction>() {

    public val state: RestartableStateFlow<PhoneState>
        field = MutableStateFlow(PhoneState()).onStartStateIn { it }

    override fun action(action: PhoneAction): Unit = when (action) {
        is PhoneAction.SetPhone -> setPhone(action.countryCode, action.phone, action.isValid)
        PhoneAction.Confirm -> confirm()
    }

    private fun setPhone(countryCode: String, phone: String, isValid: Boolean) =
        state.update { it.copy(countryCode = countryCode, phone = phone, isValid = isValid) }

    private fun confirm() {
        viewModelScope.launch(Dispatchers.Main) {
            if (state.value.isValid)
                router.push(
                    when (otpConfig) {
                        is HotpConfig -> Hotp("${state.value.countryCode}${state.value.phone}")
                        is TotpConfig -> Totp("${state.value.countryCode}${state.value.phone}")
                    },
                )
        }
    }
}
