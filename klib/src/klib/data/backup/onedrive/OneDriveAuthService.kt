package klib.data.backup.onedrive

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import klib.data.auth.model.BearerToken
import klib.data.backup.onedrive.model.AccessData
import klib.data.cryptography.hashSha256Blocking
import klib.data.cryptography.secureRandomBytes
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.bearer
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit

private const val BASE_URL = "https://login.microsoftonline.com"

public class OneDriveAuthService(
    private val clientId: String,
    private val redirectUri: String,
    private val httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
    private val accessData: AccessData = oneDriveAccessData(),
) {

    private val api = httpClient.ktorfit { baseUrl(BASE_URL) }.createOneDriveAuthApi()

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
        authenticateUser(
            api.getToken(
                userCode,
                accessData.codeVerifier,
                clientId,
                redirectUri,
                "files.readwrite.all",
            ),
        )

    public fun authenticateUser(initialToken: BearerToken): OneDriveGraphService {
        var token = initialToken
        return OneDriveGraphService(
            httpClient.bearer(
                loadTokens = { token },
                refreshToken = {
                    api.refreshToken(
                        clientId,
                        token.refreshToken!!,
                        "files.readwrite.all",
                    ).also { newToken -> token = newToken.copy(refreshToken = token.refreshToken) }
                },
            ),
        )
    }
}

private fun oneDriveAccessData(): AccessData {
    val bytes = secureRandomBytes(32)
    val codeVerifier = bytes.oneDriveEncodeBase64()
    val codeChallenge = codeVerifier.encodeToByteArray().hashSha256Blocking().oneDriveEncodeBase64()
    val accessData = AccessData(codeVerifier, codeChallenge)
    return accessData
}

private fun ByteArray.oneDriveEncodeBase64(): String = encodeBase64()
    .replace('+', '-')
    .replace('/', '_')
    .replace("=", "")

