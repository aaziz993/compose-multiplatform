package klib.data.backup.onedrive

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.HttpResponse
import klib.data.backup.onedrive.model.UploadResponse

internal interface OneDriveGraphApi {

    @Headers("Content-Type: application/octet-stream")
    @PUT("v1.0/me/drive/root:/{path}:/content")
    suspend fun upload(
        @Path("path") path: String,
        @Body body: ByteArray,
    ): UploadResponse

    @GET("v1.0/me/drive/root:/{path}:/content")
    suspend fun downloadMeta(@Path("path") path: String): HttpResponse
}
