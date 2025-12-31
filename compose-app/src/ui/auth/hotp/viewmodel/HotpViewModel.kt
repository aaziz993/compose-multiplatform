package ui.auth.hotp.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.auth.AuthState
import clib.presentation.viewmodel.ViewModel
import klib.auth.model.User
import klib.auth.otp.HotpGenerator
import klib.auth.otp.model.HotpConfig
import klib.auth.otp.model.OtpConfig
import klib.data.cryptography.secureRandomBytes
import klib.data.type.collections.restartableflow.RestartableStateFlow
import klib.data.type.primitives.string.encoding.encodeBase32ToString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import ui.auth.hotp.testOtpCode
import ui.navigation.presentation.Hotp

@KoinViewModel
public class HotpViewModel(
    @Provided
    private val config: OtpConfig,
    @Provided
    private val hotp: Hotp,
    private val authState: AuthState,
) : ViewModel<HotpAction>() {

    public val state: RestartableStateFlow<HotpState>
        field =
        MutableStateFlow(HotpState()).onStartStateIn { it }

    override fun action(action: HotpAction) {
        when (action) {
            is HotpAction.SetCode -> setCode(action.value)
            is HotpAction.SendNewCode -> generateCode()
            is HotpAction.Confirm -> confirm()
        }
    }

    private fun setCode(value: String): Unit = state.update { it.copy(code = value) }

    private fun generateCode() {
        state.update { HotpState() }
        val secret = secureRandomBytes(20).encodeBase32ToString()
        testOtpCode = HotpGenerator(secret, config as HotpConfig).generate(10)
    }

    private fun confirm() {
        viewModelScope.launch {
            if (state.value.code == testOtpCode)
                authState.setUser(
                    User(
                        username = "jogn.doe@gmail.com",
                        firstName = "John",
                        lastName = "Doe",
                        phone = hotp.contact,
                        email = "john.doe@domain.com",
                        imageUrl = "https://api.dicebear.com/9.x/bottts/png?seed=JohnDoe",
                        roles = setOf("User"),
                    ),
                )
        }
    }
}
