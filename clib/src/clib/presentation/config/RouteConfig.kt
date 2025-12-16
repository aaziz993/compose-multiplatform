package clib.presentation.config

import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.slideTransition
import io.ktor.http.Url
import klib.data.auth.model.AuthResource
import kotlinx.serialization.Serializable
import klib.data.config.RouteConfig
import klib.data.type.reflection.trySet
import klib.data.type.serialization.serializers.collections.SerializableAnyMap

@Serializable
public data class RouteConfig(
    override val urls: List<Url>? = null,
    override val additionalUrls: List<Url> = emptyList(),
    val metadata: SerializableAnyMap = slideTransition(),
    val additionalMetadata: SerializableAnyMap = emptyMap(),
    var enabled: Boolean = true,
    var alwaysShowLabel: Boolean = true,
    override val authResource: AuthResource? = null,
) : RouteConfig {

    public fun configure(route: BaseRoute) {
        route::urls trySet urls
        route.urls += additionalUrls
        route.metadata = metadata
        route.metadata += additionalMetadata
        route.enabled = enabled
        route.alwaysShowLabel = alwaysShowLabel
        route.authResource = authResource
    }
}
