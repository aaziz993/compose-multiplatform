package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import klib.data.backup.dropbox.model.DropboxUploadResponse

internal interface DropboxFilesApi {

    @POST("2/files/upload")
    suspend fun upload(
        @Header("Authorization") authorization: String,
        @Header("ContentType") contentType: ContentType = ContentType.Application.OctetStream,
        @Header(DropboxApiArg) apiArg: String,
        @Body body: ByteArray,
    ): DropboxUploadResponse

    @POST("2/files/download")
    suspend fun download(
        @Header("Authorization") authorization: String,
        @Header(DropboxApiArg) apiArg: String,
    ): HttpResponse
}
