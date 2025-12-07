package klib.data.backup.dropbox

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import klib.data.auth.model.BearerToken
import klib.data.backup.dropbox.model.AccessData
import klib.data.backup.dropbox.model.RefreshTokenRequest
import klib.data.backup.dropbox.model.TokenRequest
import klib.data.backup.dropbox.model.TokenResponse
import klib.data.cryptography.hashSha256Blocking
import klib.data.cryptography.randomBytes
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.bearer
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit

private const val BASE_URL = "https://api.dropbox.com"

public class OneDriveAuthService(
    private val clientId: String,
    private val httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
    private val accessData: AccessData = dropboxAccessData(),
) {

    private val api = httpClient.ktorfit { baseUrl(BASE_URL) }.createDropboxAuthApi()

    public fun generateVerifyUri(): String =
        "https://www.dropbox.com/oauth2/authorize" +
            "?client_id=$clientId" +
            "&response_type=code" +
            "&token_access_type=offline" +
            "&code_challenge=${accessData.codeChallenge}" +
            "&code_challenge_method=S256"

    public suspend fun authenticateUser(userCode: String): DropboxFilesService =
        authenticateUser(
            api.getToken(
                TokenRequest(
                    clientId,
                    userCode,
                    accessData.codeVerifier,
                ),
            ),
        )

    public fun authenticateUser(initialToken: BearerToken): DropboxFilesService {
        var token = initialToken
        return DropboxFilesService(
            httpClient.bearer(
                loadTokens = { token },
                refreshToken = {
                    api.refreshToken(
                        RefreshTokenRequest(
                            clientId,
                            token.refreshToken!!,
                        ),
                    ).also { newToken -> token = newToken.copy(refreshToken = token.refreshToken) }
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


