package klib.data.backup.onedrive

import de.jensklingenberg.ktorfit.*
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import klib.data.backup.OAuth2AccountService
import klib.data.backup.UserLinkedAccountsModel
import klib.data.backup.dropbox.DropboxApi
import klib.data.backup.onedrive.model.OneDriveAccessData
import klib.data.backup.onedrive.model.OneDriveRefreshableAccessData
import klib.data.backup.onedrive.model.toOneDriveRefreshableAccessData
import klib.data.cryptography.hashSha256Blocking
import klib.data.cryptography.randomBytes
import klib.data.net.http.client.KtorfitClient
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val CLIENT_ID: String = "8612f175-11d3-4dea-960a-d2cb89867a33"

internal const val BACKUP_PATH: String = "OpenOTP/OpenOTP.backup"

public sealed class OneDriveService : OAuth2AccountService {

    public data object Initialized : OneDriveService(), OAuth2AccountService.Initialized {

        override fun requestPermissions(): RequestedPermissions? {
            val bytes = randomBytes(32)
            val codeVerifier = bytes.oneDriveEncodeBase64()
            val codeChallenge = codeVerifier.encodeToByteArray().hashSha256Blocking().oneDriveEncodeBase64()
            val accessData = OneDriveAccessData(codeVerifier, codeChallenge)
            return RequestedPermissions(accessData)
        }
    }

    public class RequestedPermissions(
        private val accessData: OneDriveAccessData,
    ) : KtorfitClient("https://login.microsoftonline.com"), OAuth2AccountService.RequestedPermissions {

        private val oneDriveAuthApi: OneDriveAuthApi = ktorfit.createOneDriveAuthApi()

        override fun generateVerifyUri(): String =
            "https://login.microsoftonline.com/common/oauth2/v2.0/authorize" +
                "?client_id=$CLIENT_ID" +
                "&response_type=code" +
                "&redirect_uri=https%3A%2F%2Fopen-otp.procyk.in" +
                "&response_mode=fragment" +
                "&scope=offline_access+files.readwrite.all" +
                "&code_challenge=${accessData.codeChallenge}" +
                "&code_challenge_method=S256"

        override suspend fun authenticateUser(userCode: String): Authenticated =
            Authenticated(
                oneDriveAuthApi.exchangeCode(
                    code = userCode,
                    codeVerifier = accessData.codeVerifier,
                    clientId = CLIENT_ID,
                    redirectUri = "https://open-otp.procyk.in",
                    scope = "files.readwrite.all",
                ).toOneDriveRefreshableAccessData(),
            )
    }

    public class Authenticated(
        private val refreshableAccessData: OneDriveRefreshableAccessData,
    ) : KtorfitClient("https://login.microsoftonline.com"), OAuth2AccountService.Authenticated {

        private val oneDriveAuthApi: OneDriveAuthApi = ktorfit.createOneDriveAuthApi()

        override val isExpired: Boolean
            get() = Clock.System.now() >= refreshableAccessData.expiresAt

        override suspend fun refreshUserAccessToken(): Authenticated =
            Authenticated(
                oneDriveAuthApi.refreshToken(
                    refreshToken = refreshableAccessData.refreshToken,
                    clientId = CLIENT_ID,
                    scope = "files.readwrite.all",
                ).toOneDriveRefreshableAccessData(refreshableAccessData.refreshToken),
            )

        override suspend fun uploadBackupData(data: ByteArray): Boolean =
            oneDriveApi.upload(
                path = BACKUP_PATH,
                authorization = "Bearer ${refreshableAccessData.accessToken}",
                body = data,
            ).file.hashes.sha256Hash == data.oneDriveContentHash()

        override suspend fun downloadBackupData(): ByteArray? =
            oneDriveApi.downloadMeta(
                path = BACKUP_PATH,
                authorization = "Bearer ${refreshableAccessData.accessToken}",
            ).let { response ->
                val downloadLocation = response.headers[HttpHeaders.ContentLocation] ?: return null
                httpClient.request {
                    method = HttpMethod.Get
                    url(downloadLocation)
                }.readRawBytes()
            }

        override fun updateUserLinkedAccounts(linkedAccounts: UserLinkedAccountsModel): UserLinkedAccountsModel =
            linkedAccounts.copy(onedrive = refreshableAccessData)
    }
}

private fun ByteArray.oneDriveEncodeBase64(): String = encodeBase64()
    .replace('+', '-')
    .replace('/', '_')
    .replace("=", "")

private fun ByteArray.oneDriveContentHash(): String =
    hashSha256Blocking().toHexString(HexFormat.UpperCase)
