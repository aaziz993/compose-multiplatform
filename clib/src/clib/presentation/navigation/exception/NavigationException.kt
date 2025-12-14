package clib.presentation.navigation.exception

import clib.presentation.navigation.NavigationAction

public class NavigationException(action: NavigationAction, cause: Throwable)
    : Throwable("Navigation action '$action'", cause)
