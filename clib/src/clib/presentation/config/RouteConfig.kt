package clib.presentation.config

import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.slideTransition
import io.ktor.http.Url
import klib.data.auth.model.AuthResource
import kotlinx.serialization.Serializable
import klib.data.config.RouteConfig
import klib.data.type.serialization.serializers.collections.SerializableAnyMap

@Serializable
public data class RouteConfig(
    override val urls: List<Url> = emptyList(),
    public val metadata: SerializableAnyMap = slideTransition(),
    override val authResource: AuthResource? = null,
) : RouteConfig {

    public fun configure(route: BaseRoute) {
        route.urls += urls
        route.metadata += metadata

        if (route.authResource == null) route.authResource = authResource
    }
}
