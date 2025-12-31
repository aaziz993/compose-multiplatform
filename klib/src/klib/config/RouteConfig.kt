package klib.config

import io.ktor.http.Url
import klib.auth.model.AuthResource

public interface RouteConfig {

    public val urls: List<Url>?
    public val additionalUrls: List<Url>
    public val authResource: AuthResource?
}
