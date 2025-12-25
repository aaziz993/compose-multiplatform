package ui.auth.emal.presentation.viewmodel

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
import ui.navigation.presentation.Email
import ui.navigation.presentation.Hotp
import ui.navigation.presentation.Totp

@KoinViewModel
public class EmailViewModel(
    @Provided
    private val email: Email,
    @Provided
    private val router: Router,
    @Provided
    private val otpConfig: OtpConfig,
) : ViewModel<EmailAction>() {

    public val state: RestartableStateFlow<EmailState>
        field = MutableStateFlow(EmailState()).onStartStateIn { it }

    override fun action(action: EmailAction): Unit = when (action) {
        is EmailAction.SetEmail -> setEmail(action.value)
        EmailAction.Confirm -> confirm()
    }

    private fun setEmail(value: String) =
        state.update { it.copy(email = value) }

    private fun confirm() {
        viewModelScope.launch(Dispatchers.Main) {
            router.push(
                when (otpConfig) {
                    is HotpConfig -> Hotp(email.username, state.value.email)
                    is TotpConfig -> Totp(email.username, state.value.email)
                },
            )
        }
    }
}
