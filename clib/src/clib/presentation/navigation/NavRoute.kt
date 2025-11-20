package clib.presentation.navigation

import androidx.navigation3.runtime.NavKey

public interface NavRoute : NavKey {
    @Suppress("UNCHECKED_CAST")
    public val route: BaseRoute
        get() = requireNotNull(this as? BaseRoute) { "No route" }
}
