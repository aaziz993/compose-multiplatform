package ui.navigation.presentation.viewmodel

public sealed interface NavAction {
    public data class OpenDrawer(val value: Boolean) : NavAction
}
