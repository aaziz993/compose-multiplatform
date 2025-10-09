package ui.auth.otp.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.auth.viewmodel.UserAction
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.User
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
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
        field = viewModelMutableStateFlow(OtpState(timer = duration))

    init {
        startTimer()
    }

    private fun startTimer() =
        viewModelScope.launch {
            flow {
                while (true) {
                    delay(1.seconds)
                    emit(Unit)
                }
            }.collect { _ ->
                state.update { current ->
                    val newTimer = (current.timer - 1.seconds).coerceAtLeast(Duration.ZERO)
                    current.copy(timer = newTimer)
                }
            }
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
        state.update { it.copy(timer = duration) }
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
