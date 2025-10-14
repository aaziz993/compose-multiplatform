package ui.auth.phonecheckup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.components.navigation.Navigator
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.coroutines.StandardDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.navigation.presentation.Destination
import ui.navigation.presentation.Otp

@KoinViewModel
public class PhoneCheckUpViewModel(
    private val navigator: Navigator<Destination>
) : AbstractViewModel<PhoneCheckUpAction>() {

    public val state: RestartableStateFlow<PhoneCheckUpState>
        field = MutableStateFlow(PhoneCheckUpState()).onStartStateIn { it }

    override fun action(action: PhoneCheckUpAction): Unit = when (action) {
        is PhoneCheckUpAction.SetPhone -> setPhone(action.countryCode, action.number, action.isValid)
        PhoneCheckUpAction.Confirm -> confirm()
    }

    private fun setPhone(countryCode: String, number: String, isValid: Boolean) =
        state.update { it.copy(countryCode = countryCode, number = number, isValid = isValid) }

    private fun confirm() {
        viewModelScope.launch(StandardDispatchers.io) {
            if (state.value.isValid)
                navigator.navigate(Otp("${state.value.countryCode}${state.value.number}"))
        }
    }
}
