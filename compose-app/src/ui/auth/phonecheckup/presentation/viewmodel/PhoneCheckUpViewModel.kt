package ui.auth.phonecheckup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.components.navigation.Navigator
import clib.presentation.viewmodel.AbstractViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.navigation.presentation.Destination

@KoinViewModel
public class PhoneCheckUpViewModel(
    private val navigator: Navigator<Destination>
) : AbstractViewModel<PhoneCheckUpAction>() {

    public val state: RestartableStateFlow<PhoneCheckUpState>
        field = viewModelMutableStateFlow(PhoneCheckUpState())

    override fun action(action: PhoneCheckUpAction): Unit = when (action) {
        is PhoneCheckUpAction.SetPhone -> setPhone(action.value)
        PhoneCheckUpAction.Confirm -> confirm()
    }

    private fun setPhone(value: String) = state.update { it.copy(phone = value) }

    private fun confirm() {
        viewModelScope.launch {

        }
    }
}
