package clib.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.serializer

public interface NavRoute : NavKey {

    public val name: String
        get() = this::class.serializer().descriptor.serialName

    @Suppress("UNCHECKED_CAST")
    public val route: BaseRoute
        get() = requireNotNull(this as? BaseRoute) { "No route in '$this'" }
}
