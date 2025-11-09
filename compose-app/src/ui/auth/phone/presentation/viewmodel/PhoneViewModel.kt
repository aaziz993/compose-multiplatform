package ui.auth.phone.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.components.navigation.stateholder.NavigationAction
import clib.presentation.components.navigation.stateholder.NavigationStateHolder
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.coroutines.StandardDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.navigation.presentation.Otp

@KoinViewModel
public class PhoneViewModel(
    private val navigator: NavigationStateHolder
) : AbstractViewModel<PhoneAction>() {

    public val state: RestartableStateFlow<PhoneState>
        field = MutableStateFlow(PhoneState()).onStartStateIn { it }

    override fun action(action: PhoneAction): Unit = when (action) {
        is PhoneAction.SetPhone -> setPhone(action.countryCode, action.number, action.isValid)
        PhoneAction.Confirm -> confirm()
    }

    private fun setPhone(countryCode: String, number: String, isValid: Boolean) =
        state.update { it.copy(countryCode = countryCode, number = number, isValid = isValid) }

    private fun confirm() {
        viewModelScope.launch(StandardDispatchers.io) {
            if (state.value.isValid)
                navigator.action(
                    NavigationAction.Navigate(
                        Otp("${state.value.countryCode}${state.value.number}"),
                    ),
                )
        }
    }
}
