@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.net.http.client

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpClientPlugin
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
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.parameters
import io.ktor.serialization.ContentConverter
import io.ktor.util.AttributeKey
import io.ktor.utils.io.InternalAPI
import klib.data.auth.client.model.BearerToken
import klib.data.net.http.client.model.Pin
import kotlinx.serialization.json.Json

private val PLUGIN_CONFIGURATIONS: MutableMap<AttributeKey<*>, Any> = mutableMapOf()

public val HTTP_CLIENT_JSON: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
}

public expect fun createHttpClient(
    pins: List<Pin> = emptyList(),
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient

public fun <TBuilder : Any, TPlugin : Any> HttpClientConfig<*>.installConfigAware(
    plugin: HttpClientPlugin<TBuilder, TPlugin>,
    configure: TBuilder.() -> Unit = {}
): Unit = install(plugin) {
    configure()
    PLUGIN_CONFIGURATIONS[plugin.key] = this
}

@Suppress("UnusedReceiverParameter", "UNCHECKED_CAST")
public fun <TConfig : Any> HttpClient.pluginConfig(plugin: HttpClientPlugin<TConfig, *>): TConfig? =
    PLUGIN_CONFIGURATIONS[plugin.key] as TConfig?

public fun HttpClient.converters(contentType: ContentType): List<ContentConverter>? =
    pluginConfig(ContentNegotiation)
        ?.registrations
        ?.filter { registration -> registration.contentTypeMatcher.contains(contentType) }
        ?.map { registration -> registration.converter }

public fun HttpClient.converter(contentType: ContentType): ContentConverter =
    converters(contentType)?.firstOrNull()
        ?: error("No suitable converter for $contentType")

public suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> = try {
    val response = request { block() }
    Result.success(response.body())
}
catch (e: Throwable) {
    Result.failure(e)
}

public suspend inline fun HttpClient.safeHttpRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<HttpResponse> = try {
    val response = request { block() }
    Result.success(response)
}
catch (e: Throwable) {
    Result.failure(e)
}

public fun HttpClient.ktorfit(block: Ktorfit.Builder.() -> Unit = {}): Ktorfit =
    Ktorfit.Builder().httpClient(this).apply(block).build()

public fun HttpClient.auth(block: AuthConfig.() -> Unit): HttpClient = config {
    install(Auth, block)
}

@OptIn(InternalAPI::class)
public fun HttpClient.clearBasicToken() {
    authProvider<BasicAuthProvider>()?.clearToken()
}

@OptIn(InternalAPI::class)
public fun HttpClient.clearDigestToken() {
    authProvider<DigestAuthProvider>()?.clearToken()
}

public fun HttpClient.clearBearerToken() {
    authProvider<BearerAuthProvider>()?.clearToken()
}

public fun HttpClient.basic(
    credentials: suspend () -> BasicAuthCredentials?,
    block: BasicAuthConfig.() -> Unit = {},
): HttpClient = auth {
    basic {
        credentials {
            credentials()?.let { credentials ->
                BasicAuthCredentials(credentials.username, credentials.password)
            }
        }

        block()
    }
}

public fun HttpClient.digest(
    credentials: suspend () -> DigestAuthCredentials?,
    block: DigestAuthConfig.() -> Unit = {},
): HttpClient = auth {
    digest {
        credentials {
            credentials()?.let { credentials ->
                DigestAuthCredentials(credentials.username, credentials.password)
            }
        }

        block()
    }
}

public inline fun <reified T : BearerToken> HttpClient.bearer(
    crossinline loadTokens: suspend () -> T?,
    crossinline block: BearerAuthConfig.() -> Unit = {},
): HttpClient = auth {
    bearer {
        loadTokens {
            loadTokens()?.let { token ->
                BearerTokens(token.accessToken, token.refreshToken)
            }
        }

        block()
    }
}

public inline fun <reified T : BearerToken> HttpClient.bearer(
    crossinline loadTokens: suspend () -> T?,
    crossinline refreshToken: suspend () -> T,
    crossinline invalidRefreshToken: suspend () -> Unit = {},
    crossinline block: BearerAuthConfig.() -> Unit = {},
): HttpClient = bearer(loadTokens) {
    refreshTokens {
        try {
            refreshToken().let { token ->
                BearerTokens(token.accessToken, token.refreshToken ?: oldTokens?.refreshToken)
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

public suspend inline fun <reified T : BearerToken> RefreshTokensParams.refreshToken(
    url: String,
    clientId: String
): T = client.submitForm(
    url,
    parameters {
        append("grant_type", "refresh_token")
        append("client_id", clientId)
        append("refresh_token", requireNotNull(oldTokens?.refreshToken))
    },
) { markAsRefreshTokenRequest() }.body<T>()


