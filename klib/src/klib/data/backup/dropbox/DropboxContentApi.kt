package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse
import klib.data.backup.dropbox.model.UploadResponse

private const val DROPBOX_API_ARG: String = "Dropbox-Api-Arg"

internal interface DropboxContentApi {

    @Headers("Content-Type: application/octet-stream")
    @POST("2/files/upload")
    suspend fun upload(
        @Header(DROPBOX_API_ARG) apiArg: String,
        @Body body: ByteArray,
    ): UploadResponse

    @POST("2/files/download")
    suspend fun download(@Header(DROPBOX_API_ARG) apiArg: String): HttpResponse
}


