package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import klib.data.backup.dropbox.model.RefreshTokenRequest
import klib.data.backup.dropbox.model.TokenRequest
import klib.data.backup.dropbox.model.TokenResponse

internal interface DropboxAuthApi {

    @POST("oauth2/token")
    suspend fun getToken(
        @Body request: TokenRequest,
    ): TokenResponse

    @POST("oauth2/token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest,
    ): TokenResponse
}
