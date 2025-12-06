package klib.data.backup.onedrive

import io.ktor.client.HttpClient
import io.ktor.util.encodeBase64
import klib.data.backup.onedrive.model.OneDriveAccessData
import klib.data.cryptography.hashSha256Blocking
import klib.data.cryptography.randomBytes
import klib.data.net.http.client.bearer
import klib.data.net.http.client.ktorfit

public class OneDriveAuthService(
    baseUrl: String,
    private val httpClient: HttpClient,
    private val clientId: String,
    private val redirectUri: String,
    private val accessData: OneDriveAccessData = oneDriveAccessData(),
) {

    private val api: OneDriveAuthApi =
        httpClient.ktorfit { baseUrl(baseUrl) }.createOneDriveAuthApi()

    public fun generateVerifyUri(): String =
        "https://login.microsoftonline.com/common/oauth2/v2.0/authorize" +
            "?client_id=$clientId" +
            "&response_type=code" +
            "&redirect_uri=$redirectUri" +
            "&response_mode=fragment" +
            "&scope=offline_access+files.readwrite.all" +
            "&code_challenge=${accessData.codeChallenge}" +
            "&code_challenge_method=S256"

    public suspend fun authenticateUser(userCode: String): OneDriveGraphService =
        api.getToken(
            code = userCode,
            codeVerifier = accessData.codeVerifier,
            clientId = clientId,
            redirectUri = redirectUri,
            scope = "files.readwrite.all",
        ).let { token ->
            OneDriveGraphService(
                httpClient.bearer(
                    { token },
                    refreshToken = {
                        api.refreshToken(
                            refreshToken = token.refreshToken!!,
                            clientId = clientId,
                            scope = "files.readwrite.all",
                        )
                    },
                ),
            )
        }
}

private fun oneDriveAccessData(): OneDriveAccessData {
    val bytes = randomBytes(32)
    val codeVerifier = bytes.oneDriveEncodeBase64()
    val codeChallenge = codeVerifier.encodeToByteArray().hashSha256Blocking().oneDriveEncodeBase64()
    val accessData = OneDriveAccessData(codeVerifier, codeChallenge)
    return accessData
}

private fun ByteArray.oneDriveEncodeBase64(): String = encodeBase64()
    .replace('+', '-')
    .replace('/', '_')
    .replace("=", "")

