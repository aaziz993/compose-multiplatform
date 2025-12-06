package klib.data.backup.dropbox

import io.ktor.client.HttpClient
import io.ktor.util.encodeBase64
import klib.data.backup.dropbox.model.AccessData
import klib.data.cryptography.hashSha256Blocking
import klib.data.cryptography.randomBytes
import klib.data.net.http.client.bearer
import klib.data.net.http.client.ktorfit

public class OneDriveAuthService(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val accessData: AccessData = dropboxAccessData(),
) {

    private val api: DropboxAuthApi =
        httpClient.ktorfit { baseUrl("https://api.dropbox.com") }.createDropboxAuthApi()

    public fun generateVerifyUri(): String =
        "https://www.dropbox.com/oauth2/authorize" +
            "?client_id=$clientId" +
            "&response_type=code" +
            "&token_access_type=offline" +
            "&code_challenge=${accessData.codeChallenge}" +
            "&code_challenge_method=S256"

    public suspend fun authenticateUser(userCode: String): DropboxFilesService =
        api.getToken(
            mapOf(
                "code" to userCode,
                "grant_type" to "authorization_code",
                "code_verifier" to accessData.codeVerifier,
                "client_id" to clientId,
            ),
        ).let { token ->
            DropboxFilesService(
                httpClient.bearer(
                    { token },
                    refreshToken = {
                        api.refreshToken(
                            mapOf(
                                "grant_type" to "refresh_token",
                                "refresh_token" to token.refreshToken!!,
                                "client_id" to clientId,
                            ),
                        )
                    },
                ),
            )
        }
}

private fun dropboxAccessData(): AccessData {
    val bytes = randomBytes(32)
    val codeVerifier = bytes.dropboxEncodeBase64()
    val codeChallenge = codeVerifier.encodeToByteArray().hashSha256Blocking().dropboxEncodeBase64()
    val accessData = AccessData(codeVerifier, codeChallenge)
    return accessData
}

private fun ByteArray.dropboxEncodeBase64(): String = encodeBase64()
    .replace('+', '-')
    .replace('/', '_')
    .replace("=", "")


