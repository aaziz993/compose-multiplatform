package klib.data.config

public interface UIConfig {

    public val startRoute: String?
    public val authRedirectRoute: String?
    public val authRoute: String?
    public val routes: Map<String, RouteConfig>
    public val databaseName: String?
}
