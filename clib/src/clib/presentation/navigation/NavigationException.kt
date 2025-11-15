package clib.presentation.navigation

/**
 * Represents an error that occurred during navigation.
 *
 * @property action The navigation action that caused the error
 * @property cause The underlying exception (optional)
 */
public class NavigationException(
    public val action: NavigationAction,
    cause: Throwable,
) : RuntimeException("Navigation failed for action: $action", cause)
