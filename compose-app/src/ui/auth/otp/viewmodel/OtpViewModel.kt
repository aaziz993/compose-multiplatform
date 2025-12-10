package ui.auth.otp.viewmodel

import androidx.lifecycle.viewModelScope
import klib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.AuthState
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.model.User
import klib.data.auth.otp.model.OtpConfig
import klib.data.auth.otp.model.TotpConfig
import klib.data.type.primitives.time.CountDownTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import ui.auth.otp.testOtpCode
import ui.navigation.presentation.Otp

@KoinViewModel
public class OtpViewModel(
    private val authState: AuthState,
    @Provided
    private val otp: Otp,
    @Provided
    private val config: OtpConfig,
) : ViewModel<OtpAction>() {

    public val state: RestartableStateFlow<OtpState>
        field = MutableStateFlow(OtpState(countdown = (config as? TotpConfig)?.period ?: 0.seconds))

    private var countDownTimer: CountDownTimer? = null

    init {
        if (config is TotpConfig) startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = CountDownTimer(
            initial = (config as TotpConfig).period,
            onTick = { remaining ->
                state.update { it.copy(countdown = remaining) }
            },
            onFinish = {
                state.update { it.copy(code = "", countdown = Duration.ZERO) }
            },
        ).also { timer ->
            // Automatically cancel when viewModelScope is canceled.
            viewModelScope.coroutineContext[Job]?.invokeOnCompletion {
                timer.cancel()
            }

            timer.start()
        }
    }

    override fun action(action: OtpAction) {
        when (action) {
            is OtpAction.SetCode -> setCode(action.value)
            is OtpAction.ResendCode -> resendCode()
            is OtpAction.Confirm -> confirm()
        }
    }

    private fun setCode(value: String): Unit = state.update { it.copy(code = value) }

    public fun resendCode() {
        state.update { OtpState() }
        startTimer()
    }

    private fun confirm() {
        viewModelScope.launch {
            if (state.value.code == testOtpCode)
                authState.setUser(
                    User(
                        username = "jogn.doe@gmail.com",
                        firstName = "John",
                        lastName = "Doe",
                        phone = otp.phone,
                        roles = setOf("User"),
                    ),
                )
        }
    }
}
