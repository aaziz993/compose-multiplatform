package klib.data.auth.client.oauth

import io.ktor.client.HttpClient
import io.ktor.http.Url
import klib.data.auth.client.oauth.model.OAuthAccessTokenResponse

public abstract class AbstractOAuth2Provider(
    name: String?,
    httpClient: HttpClient,
    callbackRedirectUrl: String,
    onRedirectAuthenticate: suspend (url: Url) -> Unit
) : AbstractOAuthProvider<OAuthAccessTokenResponse.OAuth2>(
    name,
    httpClient,
    callbackRedirectUrl,
    onRedirectAuthenticate,
)
