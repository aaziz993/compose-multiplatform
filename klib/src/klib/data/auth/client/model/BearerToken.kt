package klib.data.auth.client.model

public interface BearerToken {

    public val token: String
    public val refreshToken: String?
        get() = null
}
