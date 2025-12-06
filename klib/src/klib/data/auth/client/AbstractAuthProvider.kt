package klib.data.auth.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BasicAuthConfig
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.plugins.auth.providers.BearerAuthConfig
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.DigestAuthConfig
import io.ktor.client.plugins.auth.providers.DigestAuthCredentials
import io.ktor.client.plugins.auth.providers.DigestAuthProvider
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.auth.providers.digest
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Url
import io.ktor.http.parameters
import io.ktor.utils.io.InternalAPI
import klib.data.auth.client.model.BearerToken

public fun HttpClient.auth(block: AuthConfig.() -> Unit): HttpClient = config {
    install(Auth, block)
}

@OptIn(InternalAPI::class)
public fun HttpClient.clearAuthToken() {
    authProvider<BasicAuthProvider>()?.clearToken()
    authProvider<DigestAuthProvider>()?.clearToken()
    authProvider<BearerAuthProvider>()?.clearToken()
}

public fun HttpClient.basic(
    provider: String? = null,
    getCredentials: suspend (provider: String?) -> BasicAuthCredentials?,
    block: BasicAuthConfig.() -> Unit = {},
): HttpClient = auth {
    basic {
        credentials {
            getCredentials(provider)?.let { credentials ->
                BasicAuthCredentials(credentials.username, credentials.password)
            }
        }

        block()
    }
}

public fun HttpClient.digest(
    provider: String? = null,
    getCredentials: suspend (provider: String?) -> DigestAuthCredentials?,
    block: DigestAuthConfig.() -> Unit = {},
): HttpClient = auth {
    digest {
        credentials {
            getCredentials(provider)?.let { credentials ->
                DigestAuthCredentials(credentials.username, credentials.password)
            }
        }

        block()
    }
}

public inline fun <reified T : BearerToken> HttpClient.bearer(
    provider: String? = null,
    crossinline getToken: suspend (provider: String?) -> T?,
    refreshTokenUrl: String,
    clientId: String,
    crossinline invalidRefreshToken: suspend () -> Unit = {},
    crossinline block: BearerAuthConfig.() -> Unit,
): HttpClient = auth {
    bearer {
        loadTokens {
            getToken(provider)?.let { token ->
                BearerTokens(token.token, token.refreshToken)
            }
        }

        refreshTokens {
            try {
                refreshToken<T>(
                    refreshTokenUrl,
                    clientId,
                )?.let { token ->
                    BearerTokens(token.token, oldTokens?.refreshToken!!)
                }
            }
            catch (_: Throwable) {
                null
            }.also { token ->
                // Handle invalid refresh token.
                if (token == null) invalidRefreshToken()
            }
        }

        block()
    }
}

@PublishedApi
internal suspend inline fun <reified T : BearerToken> RefreshTokensParams.refreshToken(
    url: String,
    clientId: String
): T? = client.submitForm(
    url,
    parameters {
        append("grant_type", "refresh_token")
        append("client_id", clientId)
        append("refresh_token", oldTokens?.refreshToken.orEmpty())
    },
) { markAsRefreshTokenRequest() }.body<T>()

public inline fun <reified T : BearerToken> HttpClient.oauth2(
    provider: String? = null,
    crossinline getToken: suspend (provider: String?) -> T?,
    refreshTokenUrl: String,
    clientId: String,
    crossinline invalidRefreshToken: suspend () -> Unit = {},
    callbackRedirectUrl: String,
    crossinline onRedirectAuthenticate: suspend (url: Url) -> Unit,
    crossinline block: BearerAuthConfig.() -> Unit,
): HttpClient = bearer<T>(
    provider,
    {

        null
    },
    refreshTokenUrl,
    clientId,
    invalidRefreshToken,
    block,
)
