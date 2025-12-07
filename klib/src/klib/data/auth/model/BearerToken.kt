package klib.data.auth.model

public interface BearerToken {

    public val accessToken: String
    public val refreshToken: String?
        get() = null
}
