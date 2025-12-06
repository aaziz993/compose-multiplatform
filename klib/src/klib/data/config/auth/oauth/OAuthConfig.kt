package klib.data.config.auth.oauth

public interface OAuthConfig {

    public val provider: String
    public val address: String
    public val realm: String
    public val clientId: String
}
