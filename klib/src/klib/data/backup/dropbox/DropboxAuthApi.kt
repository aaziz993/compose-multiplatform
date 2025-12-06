package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST
import klib.data.backup.dropbox.model.DropboxOAuth2RefreshableTokenResponse
import klib.data.backup.dropbox.model.DropboxOAuth2TokenResponse

internal interface DropboxAuthApi {

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun exchangeCode(
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code_verifier") codeVerifier: String,
        @Field("client_id") clientId: String,
    ): DropboxOAuth2RefreshableTokenResponse

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
    ): DropboxOAuth2TokenResponse
}
