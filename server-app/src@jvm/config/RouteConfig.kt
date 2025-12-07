package config

import io.ktor.http.Url
import klib.data.auth.model.AuthResource
import klib.data.config.RouteConfig
import kotlinx.serialization.Serializable

@Serializable
public data class RouteConfig(
    override val urls: List<Url> = emptyList(),
    override val authResource: AuthResource? = null,
) : RouteConfig
