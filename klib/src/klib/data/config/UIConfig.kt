package klib.data.config

import io.ktor.http.Url

public interface UIConfig {

    public val startRoute: Url?
    public val authRedirectRoute: Url?
    public val authRoute: Url?
    public val routes: Map<String, RouteConfig>
    public val databaseName: String?
}
