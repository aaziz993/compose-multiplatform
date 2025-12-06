package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.http.FieldMap
import de.jensklingenberg.ktorfit.http.POST
import klib.data.backup.dropbox.model.TokenResponse

internal interface DropboxAuthApi {

    @POST("oauth2/token")
    suspend fun getToken(
        @FieldMap fields: Map<String, String>,
    ): TokenResponse

    @POST("oauth2/token")
    suspend fun refreshToken(
        @FieldMap fields: Map<String, String>,
    ): TokenResponse
}
