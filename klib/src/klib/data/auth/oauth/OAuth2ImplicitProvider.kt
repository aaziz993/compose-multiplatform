package klib.data.auth.oauth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.content.TextContent
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import io.ktor.http.parseUrlEncodedParameters
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.GenerateOnlyNonceManager
import io.ktor.util.NonceManager
import io.ktor.util.encodeBase64
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import klib.data.auth.oauth.model.AuthenticationFailedCause
import klib.data.auth.oauth.model.OAuth2Exception
import klib.data.auth.oauth.model.OAuth2RedirectError
import klib.data.auth.oauth.model.OAuth2RequestParameters
import klib.data.auth.oauth.model.OAuth2ResponseParameters
import klib.data.auth.oauth.model.OAuthAccessTokenResponse
import klib.data.auth.oauth.model.OAuthGrantTypes
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

public class OAuth2Implicit(
    public val authorizeUrl: String,
    public val accessTokenUrl: String,
    public val requestMethod: HttpMethod = HttpMethod.Get,
    private val clientId: String,
    public val clientSecret: String,
    public val defaultScopes: List<String> = emptyList(),
    public val accessTokenRequiresBasicAuth: Boolean = false,
    public val nonceManager: NonceManager = GenerateOnlyNonceManager,
    public val authorizeUrlInterceptor: URLBuilder.() -> Unit = {},
    public val passParamsInURL: Boolean = false,
    public val extraAuthParameters: List<Pair<String, String>> = emptyList(),
    public val extraTokenParameters: List<Pair<String, String>> = emptyList(),
    public val accessTokenInterceptor: HttpRequestBuilder.() -> Unit = {},
    public val callbackRedirectUrl: String,
    override val onRedirectAuthenticate: suspend (url: Url) -> Unit,
    private val httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
) : OAuthProvider<OAuthAccessTokenResponse.OAuth2>() {

    // Implements Resource Owner Password Credentials Grant.
    // Takes UserPasswordCredential and validates it using OAuth2 sequence, provides OAuthAccessTokenResponse.
    // OAuth2 if succeeds.
    public suspend fun requestToken(
        username: String,
        password: String,
    ): OAuthAccessTokenResponse.OAuth2 = oauth2RequestAccessToken(
        httpClient,
        HttpMethod.Post,
        usedRedirectUrl = null,
        baseUrl = accessTokenUrl,
        clientId = clientId,
        clientSecret = clientSecret,
        code = null,
        state = null,
        configure = accessTokenInterceptor,
        extraParameters = listOf(
            OAuth2RequestParameters.UserName to username,
            OAuth2RequestParameters.Password to password,
        ),
        useBasicAuth = true,
        nonceManager = nonceManager,
        passParamsInURL = passParamsInURL,
        grantType = OAuthGrantTypes.Password,
    )

    override suspend fun getRedirectUrl(): Url = redirectAuthenticateOAuth2(
        authorizeUrl,
        callbackRedirectUrl,
        clientId,
        nonceManager.newNonce(),
        defaultScopes,
        extraAuthParameters,
        authorizeUrlInterceptor,
    )

    override suspend fun callback(parameters: Parameters): AuthenticationFailedCause? {
        val code = parameters[OAuth2RequestParameters.Code]
        val state = parameters[OAuth2RequestParameters.State]
        val error = parameters[OAuth2RequestParameters.Error]
        val errorDescription = parameters[OAuth2RequestParameters.ErrorDescription]

        return when {
            error != null -> OAuth2RedirectError(error, errorDescription)
            code != null && state != null -> try {
                resume(
                    oauth2RequestAccessToken(
                        httpClient,
                        requestMethod,
                        callbackRedirectUrl,
                        accessTokenUrl,
                        clientId,
                        clientSecret,
                        state,
                        code,
                        extraTokenParameters,
                        accessTokenInterceptor,
                        accessTokenRequiresBasicAuth,
                        nonceManager,
                        passParamsInURL,
                    ),
                )

                null
            }
            catch (_: OAuth2Exception.InvalidGrant) {
                AuthenticationFailedCause.InvalidCredentials
            }
            catch (cause: Throwable) {
                AuthenticationFailedCause.Error("Failed to request OAuth2 access token due to $cause")
                null
            }

            else -> AuthenticationFailedCause.NoCredentials
        }
    }
}

private fun redirectAuthenticateOAuth2(
    authenticateUrl: String,
    callbackRedirectUrl: String,
    clientId: String,
    state: String,
    scopes: List<String>,
    parameters: List<Pair<String, String>>,
    interceptor: URLBuilder.() -> Unit
): Url {
    val url = URLBuilder()
    url.takeFrom(authenticateUrl)
    url.parameters.apply {
        append(OAuth2RequestParameters.ClientId, clientId)
        append(OAuth2RequestParameters.RedirectUri, callbackRedirectUrl)
        if (scopes.isNotEmpty()) {
            append(OAuth2RequestParameters.Scope, scopes.joinToString(" "))
        }
        append(OAuth2RequestParameters.State, state)
        append(OAuth2RequestParameters.ResponseType, "code")
        parameters.forEach { (k, v) -> append(k, v) }
    }
    interceptor(url)
    return url.build()
}

private suspend fun oauth2RequestAccessToken(
    client: HttpClient,
    method: HttpMethod,
    usedRedirectUrl: String?,
    baseUrl: String,
    clientId: String,
    clientSecret: String,
    state: String?,
    code: String?,
    extraParameters: List<Pair<String, String>> = emptyList(),
    configure: HttpRequestBuilder.() -> Unit = {},
    useBasicAuth: Boolean = false,
    nonceManager: NonceManager,
    passParamsInURL: Boolean = false,
    grantType: String = OAuthGrantTypes.AuthorizationCode
): OAuthAccessTokenResponse.OAuth2 {
    if (!nonceManager.verifyNonce(state.orEmpty())) {
        throw OAuth2Exception.InvalidNonce()
    }

    val request = HttpRequestBuilder()
    request.url.takeFrom(baseUrl)

    val urlParameters = ParametersBuilder().apply {
        append(OAuth2RequestParameters.ClientId, clientId)
        append(OAuth2RequestParameters.ClientSecret, clientSecret)
        append(OAuth2RequestParameters.GrantType, grantType)
        if (state != null) {
            append(OAuth2RequestParameters.State, state)
        }
        if (code != null) {
            append(OAuth2RequestParameters.Code, code)
        }
        if (usedRedirectUrl != null) {
            append(OAuth2RequestParameters.RedirectUri, usedRedirectUrl)
        }
        extraParameters.forEach { (k, v) -> append(k, v) }
    }.build()

    when (method) {
        HttpMethod.Get -> request.url.parameters.appendAll(urlParameters)
        HttpMethod.Post -> {
            if (passParamsInURL) {
                request.url.parameters.appendAll(urlParameters)
            }
            else {
                request.setBody(
                    TextContent(
                        urlParameters.formUrlEncode(),
                        ContentType.Application.FormUrlEncoded,
                    ),
                )
            }
        }

        else -> throw UnsupportedOperationException("Method $method is not supported. Use GET or POST")
    }

    request.apply {
        this.method = method
        header(
            HttpHeaders.Accept,
            listOf(ContentType.Application.FormUrlEncoded, ContentType.Application.Json).joinToString(","),
        )
        if (useBasicAuth) {
            header(
                HttpHeaders.Authorization,
                HttpAuthHeader.Single(
                    AuthScheme.Basic,
                    "$clientId:$clientSecret".toByteArray(Charsets.ISO_8859_1).encodeBase64(),
                ).render(),
            )
        }

        configure()
    }

    val response = client.request(request)

    val body = response.bodyAsText()

    val (contentType, content) = try {
        if (response.status == HttpStatusCode.NotFound) {
            throw IOException("Access token query failed with http status 404 for the page $baseUrl")
        }
        val contentType = response.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
            ?: ContentType.Any

        Pair(contentType, body)
    }
    catch (ioe: IOException) {
        throw ioe
    }
    catch (cause: Throwable) {
        throw IOException("Failed to acquire request token due to wrong content: $body", cause)
    }

    val contentDecodeResult = Result.runCatching { decodeContent(content, contentType) }
    val errorCode = contentDecodeResult.map { it[OAuth2ResponseParameters.Error] }

    // try error code first
    errorCode.getOrNull()?.let {
        throwOAuthError(it, contentDecodeResult.getOrThrow())
    }

    // ensure status code is successful
    if (!response.status.isSuccess()) {
        throw IOException(
            "Access token query failed with http status ${response.status} for the page $baseUrl",
        )
    }

    // will fail if content decode failed but status is OK
    val contentDecoded = contentDecodeResult.getOrThrow()

    // finally, extract access token
    return OAuthAccessTokenResponse.OAuth2(
        accessToken = contentDecoded[OAuth2ResponseParameters.AccessToken]
            ?: throw OAuth2Exception.MissingAccessToken(),
        tokenType = contentDecoded[OAuth2ResponseParameters.TokenType] ?: "",
        state = state,
        expiresIn = contentDecoded[OAuth2ResponseParameters.ExpiresIn]?.toLong() ?: 0L,
        refreshToken = contentDecoded[OAuth2ResponseParameters.RefreshToken],
        extraParameters = contentDecoded,
    )
}

internal fun decodeContent(content: String, contentType: ContentType): Parameters = when {
    contentType.match(ContentType.Application.FormUrlEncoded) -> content.parseUrlEncodedParameters()
    contentType.match(ContentType.Application.Json) -> Parameters.build {
        Json.decodeFromString(JsonObject.serializer(), content).forEach { (key, element) ->
            (element as? JsonPrimitive)?.content?.let { append(key, it) }
        }
    }

    else -> {
        // some servers may respond with a wrong content type, so we have to try to guess
        when {
            content.startsWith("{") && content.trim().endsWith("}") -> decodeContent(
                content.trim(),
                ContentType.Application.Json,
            )

            content.matches("([a-zA-Z\\d_-]+=[^=&]+&?)+".toRegex()) -> decodeContent(
                content,
                ContentType.Application.FormUrlEncoded,
            ) // TODO too risky, isn't it?
            else -> throw IOException("unsupported content type $contentType")
        }
    }
}

private fun throwOAuthError(errorCode: String, parameters: Parameters): Nothing {
    val errorDescription = parameters["error_description"] ?: "OAuth2 Server responded with $errorCode"

    throw when (errorCode) {
        "invalid_grant" -> OAuth2Exception.InvalidGrant(errorDescription)
        else -> OAuth2Exception.UnknownException(errorDescription, errorCode)
    }
}
