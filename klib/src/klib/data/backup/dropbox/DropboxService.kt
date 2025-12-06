package klib.data.backup.dropbox

import de.jensklingenberg.ktorfit.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import klib.data.backup.OAuth2AccountService
import klib.data.backup.UserLinkedAccountsModel
import klib.data.backup.dropbox.model.DropboxAccessData
import klib.data.backup.dropbox.model.DropboxDownloadArg
import klib.data.backup.dropbox.model.DropboxDownloadResponse
import klib.data.backup.dropbox.model.DropboxRefreshableAccessData
import klib.data.backup.dropbox.model.DropboxUploadArg
import klib.data.backup.dropbox.model.toDropboxRefreshableAccessData
import klib.data.cryptography.hashSha256Blocking
import klib.data.cryptography.randomBytes
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.KtorfitClient
import kotlin.math.min
import kotlin.time.Clock

internal const val DropboxApiArg: String = "Dropbox-Api-Arg"
private const val DropboxApiResult: String = "Dropbox-Api-Result"

private const val CLIENT_ID: String = "gr00g1l67sjv015"

internal const val BACKUP_PATH: String = "/OpenOTP.backup"

private const val SHA256_BITS_COUNT: Int = 32

public sealed class DropboxService : KtorfitClient("https://www.dropbox.com"), OAuth2AccountService {

    protected val dropboxApi: DropboxApi = ktorfit.createDropboxApi()

    public data object Initialized : DropboxService(), OAuth2AccountService.Initialized {

        override fun requestPermissions(): RequestedPermissions? {
            val bytes = randomBytes(32)
            val codeVerifier = bytes.dropboxEncodeBase64()
            val codeChallenge = codeVerifier.encodeToByteArray().hashSha256Blocking().dropboxEncodeBase64()
            val accessData = DropboxAccessData(codeVerifier, codeChallenge)
            return RequestedPermissions(accessData)
        }
    }

    public class RequestedPermissions(
        private val accessData: DropboxAccessData,
    ) : DropboxService(), OAuth2AccountService.RequestedPermissions {

        override fun generateVerifyUri(): String =
            "https://www.dropbox.com/oauth2/authorize" +
                "?client_id=$CLIENT_ID" +
                "&response_type=code" +
                "&token_access_type=offline" +
                "&code_challenge=${accessData.codeChallenge}" +
                "&code_challenge_method=S256"

        override suspend fun authenticateUser(userCode: String): Authenticated =
            Authenticated(
                dropboxApi.exchangeCode(
                    code = userCode,
                    codeVerifier = accessData.codeVerifier,
                    clientId = CLIENT_ID,
                ).toDropboxRefreshableAccessData(),
            )
    }

    public class Authenticated(
        private val refreshableAccessData: DropboxRefreshableAccessData,
    ) : DropboxService(), OAuth2AccountService.Authenticated {

        override val isExpired: Boolean
            get() = Clock.System.now() >= refreshableAccessData.expiresAt

        override suspend fun refreshUserAccessToken(): Authenticated =
            Authenticated(
                dropboxApi.refreshToken(
                    refreshToken = refreshableAccessData.refreshToken,
                    clientId = CLIENT_ID,
                ).toDropboxRefreshableAccessData(refreshableAccessData.refreshToken),
            )

        override suspend fun uploadBackupData(data: ByteArray): Boolean =
            dropboxApi.upload(
                authorization = "Bearer ${refreshableAccessData.accessToken}",
                apiArg = data.toUploadApiArg(),
                body = data,
            ).contentHash == data.dropboxContentHash()

        override suspend fun downloadBackupData(): ByteArray? =
            dropboxApi.download(
                authorization = "Bearer ${refreshableAccessData.accessToken}",
                apiArg = downloadApiArg(),
            ).let { response ->
                val header = response.headers[DropboxApiResult] ?: return null
                val downloadResponse = HTTP_CLIENT_JSON.decodeFromString<DropboxDownloadResponse>(header)
                val bytes = response.readRawBytes()
                bytes.takeIf { it.dropboxContentHash() == downloadResponse.contentHash }
            }

        override fun updateUserLinkedAccounts(linkedAccounts: UserLinkedAccountsModel): UserLinkedAccountsModel =
            linkedAccounts.copy(dropbox = refreshableAccessData)
    }
}

private fun ByteArray.toUploadApiArg(): String =
    HTTP_CLIENT_JSON.encodeToString(DropboxUploadArg(dropboxContentHash()))

private fun downloadApiArg(): String =
    HTTP_CLIENT_JSON.encodeToString(DropboxDownloadArg())

private fun ByteArray.dropboxEncodeBase64(): String = encodeBase64()
    .replace('+', '-')
    .replace('/', '_')
    .replace("=", "")

private fun ByteArray.dropboxContentHash(blockSize: Int = 4_194_304): String {
    var offset = 0
    val hashes = mutableListOf<ByteArray>()
    while (offset <= lastIndex) {
        val end = min(offset + blockSize - 1, lastIndex)
        val len = end - offset + 1
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


