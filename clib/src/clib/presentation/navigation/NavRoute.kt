package clib.presentation.navigation

import androidx.navigation3.runtime.NavKey

public interface NavRoute : NavKey {

    @Suppress("UNCHECKED_CAST")
    public val route: BaseRoute
        get() = checkNotNull(this as? BaseRoute) { "No route" }
}
