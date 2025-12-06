package klib.data.auth.client.oauth

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.http.auth.HttpAuthHeader
import klib.data.auth.client.oauth.model.AuthenticationFailedCause
import klib.data.auth.client.oauth.model.OAuth1aException
import klib.data.auth.client.oauth.model.OAuthAccessTokenResponse
import klib.data.net.http.toUrl

public class OAuth1aExplicitProvider(
    name: String?,
    httpClient: HttpClient,
    public val loginUrl: Url,
    callbackRedirectUrl: String,
    onRedirectAuthenticate: suspend (url: Url) -> Unit,
) : AbstractOAuthProvider<OAuthAccessTokenResponse.OAuth1a>(
    name,
    httpClient,
    callbackRedirectUrl,
    onRedirectAuthenticate,
) {

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

    override suspend fun getRedirectUrl(): Url =
        httpClient.get(loginUrl).headers[HttpHeaders.Location]!!.toUrl()
}
