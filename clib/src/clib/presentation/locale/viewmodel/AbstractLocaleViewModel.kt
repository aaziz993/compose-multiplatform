package clib.presentation.locale.viewmodel

import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.location.locale.Locale
import kotlinx.coroutines.flow.update

public abstract class AbstractLocaleViewModel : AbstractViewModel<LocaleAction>() {

    protected open suspend fun initialState(): Locale? = null

    public val state: RestartableStateFlow<Locale?>
        field = viewModelMutableStateFlow(null) {
            initialState()
        }

    override fun action(action: LocaleAction): Unit = when (action) {
        is LocaleAction.SetLocale -> setLocale(action.value)
    }

    private fun setLocale(value: Locale?) = state.update { value }
}
