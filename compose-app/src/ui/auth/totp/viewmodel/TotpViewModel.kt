package ui.auth.totp.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.auth.AuthState
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.model.User
import klib.data.auth.otp.TotpGenerator
import klib.data.auth.otp.model.TotpConfig
import klib.data.cryptography.secureRandomBytes
import klib.data.type.collections.restartableflow.RestartableStateFlow
import klib.data.type.primitives.string.encoding.encodeBase32ToString
import klib.data.type.primitives.time.CountDownTimer
import klib.data.type.primitives.time.nowEpochMillis
import kotlin.time.Duration
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import ui.auth.hotp.testOtpCode
import ui.navigation.presentation.Totp

@KoinViewModel
public class TotpViewModel(
    private val authState: AuthState,
    @Provided
    private val totp: Totp,
    @Provided
    private val config: TotpConfig,
) : ViewModel<TotpAction>() {

    public val state: RestartableStateFlow<TotpState>
        field =
        MutableStateFlow(TotpState(countdown = config.period)).onStartStateIn { it }

    private var countDownTimer: CountDownTimer? = null

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = CountDownTimer(
            initial = state.value.countdown,
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

    override fun action(action: TotpAction) {
        when (action) {
            is TotpAction.SetCode -> setCode(action.value)
            is TotpAction.SendNewCode -> resendCode()
            is TotpAction.Confirm -> confirm()
        }
    }

    private fun setCode(value: String): Unit = state.update { it.copy(code = value) }

    private fun resendCode() {
        state.update { TotpState() }
        val secret = secureRandomBytes(20).encodeBase32ToString()
        testOtpCode = TotpGenerator(secret, config).generate(nowEpochMillis)
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
                        phone = totp.contact,
                        roles = setOf("User"),
                    ),
                )
        }
    }
}
