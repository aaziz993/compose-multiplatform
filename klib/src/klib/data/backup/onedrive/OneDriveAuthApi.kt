package klib.data.backup.onedrive

import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST
import klib.data.backup.onedrive.model.OneDriveOAuth2RefreshableTokenResponse
import klib.data.backup.onedrive.model.OneDriveOAuth2TokenResponse

internal interface OneDriveAuthApi {

    @FormUrlEncoded
    @POST("common/oauth2/v2.0/token")
    suspend fun exchangeCode(
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code_verifier") codeVerifier: String,
        @Field("client_id") clientId: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("scope") scope: String,
    ): OneDriveOAuth2RefreshableTokenResponse

    @FormUrlEncoded
    @POST("common/oauth2/v2.0/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("scope") scope: String,
    ): OneDriveOAuth2TokenResponse
}
