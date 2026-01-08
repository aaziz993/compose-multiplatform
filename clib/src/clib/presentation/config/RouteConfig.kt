package clib.presentation.config

import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.Route
import clib.presentation.navigation.slideTransition
import klib.auth.model.AuthResource
import klib.config.RouteConfig
import klib.data.type.serialization.serializers.collections.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
public data class RouteConfig(
    override val basePaths: List<String> = listOf(""),
    override val additionalBasePaths: List<String> = emptyList(),
    val metadata: SerializableAnyMap = slideTransition(),
    val additionalMetadata: SerializableAnyMap = emptyMap(),
    var enabled: Boolean = true,
    var alwaysShowLabel: Boolean = true,
    public val isAuth: Boolean = false,
    override val authResource: AuthResource? = null,
) : RouteConfig {

    public fun configure(route: BaseRoute) {
        route.basePaths = basePaths + additionalBasePaths
        route.metadata = metadata + additionalMetadata
        route.enabled = enabled
        route.alwaysShowLabel = alwaysShowLabel
        route.isAuth = isAuth
        (route as? Route<*>)?.authResource = authResource
    }
}
