package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse
import klib.data.backup.dropbox.model.DropboxUploadResponse

private const val DropboxApiArg: String = "Dropbox-Api-Arg"

internal interface DropboxContentApi {

    @Headers("Content-Type: application/octet-stream")
    @POST("2/files/upload")
    suspend fun upload(
        @Header(DropboxApiArg) apiArg: String,
        @Body body: ByteArray,
    ): DropboxUploadResponse

    @POST("2/files/download")
    suspend fun download(@Header(DropboxApiArg) apiArg: String): HttpResponse
}


