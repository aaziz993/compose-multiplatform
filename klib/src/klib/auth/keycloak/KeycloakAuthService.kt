package klib.auth.keycloak

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import klib.auth.model.BearerToken
import klib.data.network.http.client.HTTP_CLIENT_JSON
import klib.data.network.http.client.auth
import klib.data.network.http.client.bearer
import klib.data.network.http.client.createHttpClient
import klib.data.network.http.client.ktorfit

public class KeycloakAuthService(
    private val baseUrl: String,
    public val realm: String,
    public val clientId: String,
    private val httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
) {

    private val api = httpClient.ktorfit { baseUrl("$baseUrl/realms/$realm/protocol/openid-connect/token") }
        .createKeycloakAuthApi()

    public suspend fun authenticateUser(username: String, password: String): KeycloakAdminService =
        authenticateUser(
            api.getToken(
                clientId = clientId,
                username = username,
                password = password,
            ),
        )

    public suspend fun authenticateUser(clientSecret: String): KeycloakAdminService =
        authenticateUser(
            api.getToken(
                clientId = clientId,
                clientSecret = clientSecret,
            ),
        )

    public fun authenticateUser(initialToken: BearerToken): KeycloakAdminService {
        var token = initialToken
        return KeycloakAdminService(
            baseUrl,
            httpClient.auth {
                bearer(
                    loadTokens = { token },
                    refreshToken = {
                        api.refreshToken(
                            clientId,
                            token.refreshToken!!,
                        ).also { newToken -> token = newToken }
                    },
                )
            },
            realm,
        )
    }
}
