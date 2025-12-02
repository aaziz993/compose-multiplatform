package klib.data.config.client

import io.ktor.http.Url
import klib.data.type.auth.AuthResource
import kotlinx.serialization.Serializable

@Serializable
public data class RouteConfig(
    val urls: List<Url> = emptyList(),
    val authResource: AuthResource? = null,
)
