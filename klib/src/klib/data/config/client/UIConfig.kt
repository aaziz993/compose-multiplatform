package klib.data.config.client

public interface UIConfig {

    public val startRoute: String?
    public val authRedirectRoute: String?
    public val authRoute: String?

    //    public val auth: ClientAuthConfig
    public val databaseName: String?
    public val routes: Map<String, RouteConfig>
}
