package klib.data.backup.dropbox

import io.ktor.client.HttpClient
import io.ktor.client.statement.readRawBytes
import klib.data.backup.Backup
import klib.data.backup.dropbox.model.DropboxDownloadArg
import klib.data.backup.dropbox.model.DropboxDownloadResponse
import klib.data.backup.dropbox.model.DropboxUploadArg
import klib.data.cryptography.hashSha256Blocking
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit
import kotlin.math.min

private const val DropboxApiResult: String = "Dropbox-Api-Result"

private const val SHA256_BITS_COUNT: Int = 32

public class DropboxFilesService(
    httpClient: HttpClient = createHttpClient(),
) : Backup {

    private val dropboxGraphApi: DropboxContentApi =
        httpClient.ktorfit { baseUrl("https://content.dropboxapi.com") }.createDropboxContentApi()

    override suspend fun upload(path: String, data: ByteArray): Boolean =
        dropboxGraphApi.upload(data.toUploadApiArg(path), data).contentHash == data.dropboxContentHash()

    override suspend fun download(path: String): ByteArray? =
        dropboxGraphApi.download(downloadApiArg(path)).let { response ->
            val header = response.headers[DropboxApiResult] ?: return null
            val downloadResponse = HTTP_CLIENT_JSON.decodeFromString<DropboxDownloadResponse>(header)
            val bytes = response.readRawBytes()
            bytes.takeIf { it.dropboxContentHash() == downloadResponse.contentHash }
        }
}

private fun ByteArray.toUploadApiArg(path: String): String =
    HTTP_CLIENT_JSON.encodeToString(DropboxUploadArg(contentHash = dropboxContentHash(), path = path))

private fun downloadApiArg(path: String): String =
    HTTP_CLIENT_JSON.encodeToString(DropboxDownloadArg(path = path))

private fun ByteArray.dropboxContentHash(blockSize: Int = 4_194_304): String {
    var offset = 0
    val hashes = mutableListOf<ByteArray>()
    while (offset <= lastIndex) {
        val end = min(offset + blockSize - 1, lastIndex)
        val hash = copyOfRange(offset, end + 1).hashSha256Blocking()
        hashes += hash
        offset += blockSize
    }
    val concatenated = ByteArray(hashes.size * SHA256_BITS_COUNT) { index ->
        val blockNumber = index / SHA256_BITS_COUNT
        val indexInBlock = index - (blockNumber * SHA256_BITS_COUNT)
        val block = hashes[blockNumber]
        block[indexInBlock]
    }
    return concatenated.hashSha256Blocking().toHexString()
}


