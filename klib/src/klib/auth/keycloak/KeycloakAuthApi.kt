package klib.auth.keycloak

import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST
import klib.auth.keycloak.model.TokenResponse

public interface KeycloakAuthApi {

    @POST("/")
    @FormUrlEncoded
    public suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
    ): TokenResponse

    @POST("/")
    @FormUrlEncoded
    public suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials",
    ): TokenResponse

    @POST("/")
    @FormUrlEncoded
    public suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
    ): TokenResponse
}
