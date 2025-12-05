package klib.data.config

import io.ktor.http.Url
import klib.data.auth.AuthResource
import klib.data.type.serialization.serializers.collections.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
public data class RouteConfig(
    val urls: List<Url> = emptyList(),
    public val metadata: SerializableAnyMap = emptyMap(),
    val authResource: AuthResource? = null,
)
