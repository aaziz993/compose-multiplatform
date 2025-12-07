package klib.data.backup.onedrive

import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.readRawBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import klib.data.backup.Backup
import klib.data.cryptography.hashSha256Blocking
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit

private const val BASE_URL = "https://graph.microsoft.com"

public class OneDriveGraphService(
    private val httpClient: HttpClient = createHttpClient(),
) : Backup {

    private val oneDriveGraphApi =
        httpClient.ktorfit { baseUrl(BASE_URL) }.createOneDriveGraphApi()

    override suspend fun upload(path: String, data: ByteArray): Boolean =
        oneDriveGraphApi.upload(path, data).file.hashes.sha256Hash == data.oneDriveContentHash()

    override suspend fun download(path: String): ByteArray? =
        oneDriveGraphApi.downloadMeta(path).let { response ->
            val downloadLocation = response.headers[HttpHeaders.ContentLocation] ?: return null
            httpClient.request {
                method = HttpMethod.Get
                url(downloadLocation)
            }.readRawBytes()
        }
}

private fun ByteArray.oneDriveContentHash(): String =
    hashSha256Blocking().toHexString(HexFormat.UpperCase)
