package klib.data.backup.onedrive

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.HttpResponse
import klib.data.backup.onedrive.model.OneDriveUploadResponse

internal interface OneDriveGraphApi {

    @PUT("v1.0/me/drive/root:/{path}:/content")
    suspend fun upload(
        @Path("path") path: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/octet-stream",
        @Body body: ByteArray,
    ): OneDriveUploadResponse

    @GET("v1.0/me/drive/root:/{path}:/content")
    suspend fun downloadMeta(
        @Path("path") path: String,
        @Header("Authorization") authorization: String,
    ): HttpResponse
}
