package klib.data.auth.oauth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import klib.data.auth.oauth.model.AuthenticationFailedCause
import klib.data.auth.oauth.model.OAuth2Exception
import klib.data.auth.oauth.model.OAuth2RequestParameters
import klib.data.auth.oauth.model.OAuth2ResponseParameters
import klib.data.auth.oauth.model.OAuthAccessTokenResponse
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient

public class OAuth2ExplicitProvider(
    public val loginUrl: Url,
    public val callbackRedirectUrl: String,
    override val onRedirectAuthenticate: suspend (url: Url) -> Unit,
    private val httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
) : OAuthProvider<OAuthAccessTokenResponse.OAuth2>() {

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
