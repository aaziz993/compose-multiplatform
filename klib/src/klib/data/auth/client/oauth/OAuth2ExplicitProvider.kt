package klib.data.auth.client.oauth

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import klib.data.auth.client.oauth.model.AuthenticationFailedCause
import klib.data.auth.client.oauth.model.OAuth2Exception
import klib.data.auth.client.oauth.model.OAuth2RequestParameters
import klib.data.auth.client.oauth.model.OAuth2ResponseParameters
import klib.data.auth.client.oauth.model.OAuthAccessTokenResponse

public class OAuth2ExplicitProvider(
    name: String?,
    httpClient: HttpClient,
    public val loginUrl: Url,
    callbackRedirectUrl: String,
    onRedirectAuthenticate: suspend (url: Url) -> Unit,
) : AbstractOAuth2Provider(
    name,
    httpClient,
    callbackRedirectUrl,
    onRedirectAuthenticate,
) {

    override suspend fun getRedirectUrl(): Url =
        Url(httpClient.get(loginUrl).headers[HttpHeaders.Location]!!)

    override suspend fun callback(parameters: Parameters): AuthenticationFailedCause? =
        try {
            resume(
                OAuthAccessTokenResponse.OAuth2(
                    accessToken = parameters[OAuth2ResponseParameters.AccessToken]
                        ?: throw OAuth2Exception.MissingAccessToken(),
                    tokenType = parameters[OAuth2ResponseParameters.TokenType] ?: "",
                    state = parameters[OAuth2RequestParameters.State],
                    expiresIn = parameters[OAuth2ResponseParameters.ExpiresIn]?.toLong() ?: 0L,
                    refreshToken = parameters[OAuth2ResponseParameters.RefreshToken],
                    extraParameters = parameters,
                ),
            )
            null
        }
        catch (e: OAuth2Exception) {
            AuthenticationFailedCause.Error(e.message.orEmpty())
        }
}
