package klib.data.auth.oauth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.serialization.kotlinx.json.json
import klib.data.auth.oauth.model.AuthenticationFailedCause
import klib.data.auth.oauth.model.OAuth1aException
import klib.data.auth.oauth.model.OAuthAccessTokenResponse
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.toUrl

public class OAuth1aExplicitProvider(
    public val loginUrl: Url,
    public val callbackRedirectUrl: String,
    override val onRedirectAuthenticate: suspend (url: Url) -> Unit,
    private val httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
) : OAuthProvider<OAuthAccessTokenResponse.OAuth1a>() {

    override suspend fun getRedirectUrl(): Url =
        httpClient.get(loginUrl).headers[HttpHeaders.Location]!!.toUrl()

    override suspend fun callback(parameters: Parameters): AuthenticationFailedCause? =
        try {
            resume(
                OAuthAccessTokenResponse.OAuth1a(
                    parameters[HttpAuthHeader.Parameters.OAuthToken]
                        ?: throw OAuth1aException.MissingTokenException(),
                    parameters[HttpAuthHeader.Parameters.OAuthTokenSecret]
                        ?: throw OAuth1aException.MissingTokenException(),
                    parameters,
                ),
            )
            null
        }
        catch (e: OAuth1aException) {
            AuthenticationFailedCause.Error(e.message.orEmpty())
        }
}
