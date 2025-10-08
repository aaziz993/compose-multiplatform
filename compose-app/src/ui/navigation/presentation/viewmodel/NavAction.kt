package ui.navigation.presentation.viewmodel

public sealed interface NavAction {
    public data object ToggleDrawer : NavAction
}
