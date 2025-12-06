package klib.data.auth.client.oauth

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*
import klib.data.auth.client.oauth.model.AuthenticationFailedCause
import klib.data.auth.client.oauth.model.OAuth1aException
import klib.data.auth.client.oauth.model.OAuthAccessTokenResponse
import klib.data.cache.CoroutineCache
import klib.data.net.http.toUrl

public class OAuth1aExplicitProvider(
    name: String?,
    baseUrl: String,
    httpClient: HttpClient,
    public val loginUrl: Url,
    callbackRedirectUrl: String,
    cache: CoroutineCache<String, OAuthAccessTokenResponse.OAuth1a>,
    onRedirectAuthenticate: suspend (url: Url) -> Unit
) : AbstractOAuthProvider<OAuthAccessTokenResponse.OAuth1a>(
    name,
    baseUrl,
    httpClient,
    callbackRedirectUrl,
    cache,
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
