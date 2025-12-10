package ui.auth.phone.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.navigation.Router
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.otp.HotpGenerator
import klib.data.auth.otp.TotpGenerator
import klib.data.auth.otp.model.HotpConfig
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import klib.data.cryptography.secureRandomBytes
import klib.data.type.collections.restartableflow.RestartableStateFlow
import klib.data.type.primitives.string.encoding.encodeBase32ToString
import klib.data.type.primitives.time.nowEpochMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import ui.auth.otp.testOtpCode
import ui.navigation.presentation.Otp

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
            if (!state.value.isValid) return@launch

            val secret = secureRandomBytes(20).encodeBase32ToString()

            testOtpCode = when (otpConfig) {
                is TotpConfig -> TotpGenerator(secret, otpConfig)
                    .generate(nowEpochMillis)

                is HotpConfig -> HotpGenerator(secret, otpConfig)
                    .generate(10)
            }

            router.push(Otp("${state.value.countryCode}${state.value.phone}"))
        }
    }
}
