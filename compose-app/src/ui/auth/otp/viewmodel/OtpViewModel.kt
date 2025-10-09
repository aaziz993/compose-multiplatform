package ui.auth.otp.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.auth.viewmodel.UserAction
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.User
import klib.data.type.primitives.time.CountDownTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.auth.presentation.viewmodel.UserViewModel

@KoinViewModel
public class OtpViewModel(
    private val duration: Duration = 60.seconds,
    private val userViewModel: UserViewModel
) : AbstractViewModel<OtpAction>() {

    public val state: MutableStateFlow<OtpState>
        field = viewModelMutableStateFlow(OtpState(duration = duration))

    private var countDownTimer: CountDownTimer? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = CountDownTimer(
                initial = duration,
                interval = 1.seconds,
                onTick = { remaining ->
                    state.update { it.copy(duration = remaining) }
                },
                onFinish = {
                    state.update { it.copy(duration = Duration.ZERO) }
                },
                viewModelScope,
        ).also(CountDownTimer::start)
    }

    override fun action(action: OtpAction) {
        when (action) {
            is OtpAction.SetCode -> setCode(action.value)
            is OtpAction.ResendCode -> resendCode()
            is OtpAction.Confirm -> confirm(action.phone)
        }
    }

    private fun setCode(value: String) = viewModelScope.launch {
        state.update { it.copy(code = value) }
    }

    public fun resendCode() {
        state.update { it.copy(duration = duration) }
        startTimer()
    }

    private fun confirm(phone: String) = viewModelScope.launch {
        if (state.value.code == "1234")
            userViewModel.action(
                UserAction.SetUser(
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
