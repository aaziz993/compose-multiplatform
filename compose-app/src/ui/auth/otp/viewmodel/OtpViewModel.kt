package ui.auth.otp.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.model.User
import klib.data.type.primitives.time.CountDownTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class OtpViewModel(
    private val authStateHolder: AuthStateHolder
) : AbstractViewModel<OtpAction>() {

    public val state: RestartableStateFlow<OtpState>
        field = viewModelMutableStateFlow(OtpState())

    private var countDownTimer: CountDownTimer? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = CountDownTimer(
            initial = state.value.countdown,
            interval = 1.seconds,
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
            is OtpAction.Confirm -> confirm(action.phone)
        }
    }

    private fun setCode(value: String): Unit = state.update { it.copy(code = value) }

    public fun resendCode() {
        state.update { OtpState() }
        startTimer()
    }

    private fun confirm(phone: String) {
        viewModelScope.launch {
            if (state.value.code == "1234")
                authStateHolder.action(
                    AuthAction.SetUser(
                        User(
                            username = "jogn.doe@gmail.com",
                            firstName = "John",
                            lastName = "Doe",
                            phone = phone,
                            roles = setOf("User"),
                        ),
                    ),
                )
        }
    }
}
