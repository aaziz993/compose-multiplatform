package klib.data.backup.onedrive

import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST
import klib.data.backup.onedrive.model.TokenResponse

internal interface OneDriveAuthApi {

    @FormUrlEncoded
    @POST("common/oauth2/v2.0/token")
    suspend fun getToken(
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("client_id") clientId: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String = "authorization_code",
    ): TokenResponse

    @FormUrlEncoded
    @POST("common/oauth2/v2.0/token")
    suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String = "refresh_token",
    ): TokenResponse
}
