package clib.presentation.navigation

public class NavigationException(
    public val action: NavigationAction,
    cause: Throwable,
) : RuntimeException(
    "Navigation action '$action' failed: ${cause.message}",
    cause,
)
