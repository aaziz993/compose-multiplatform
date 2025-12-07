package klib.data.auth.firebase

import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.parameters
import klib.data.auth.client.model.BearerToken
import klib.data.auth.firebase.model.SignInWithCustomTokenRequest
import klib.data.auth.firebase.model.SignInWithIdpRequest
import klib.data.auth.firebase.model.SignRequest
import klib.data.auth.firebase.model.TokenRequest
import klib.data.net.http.client.bearer
import klib.data.net.http.client.ktorfit

public class FirebaseAccountsAuthService(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    public val apiKey: String,
) {

    private val api = httpClient.config {
        defaultRequest {
            parameters {
                append("apiKey", apiKey)
            }
        }
    }.ktorfit { baseUrl(baseUrl) }.createFirebaseAccountsApi()

    public suspend fun signInWithPassword(email: String, password: String): FirebaseAccountsService =
        authenticateUser(api.signInWithPassword(SignRequest(email, password)))

    public suspend fun signInWithCustomToken(token: String): FirebaseAccountsService =
        authenticateUser(api.signInWithCustomToken(SignInWithCustomTokenRequest(token)))

    public suspend fun signInWithIdp(
        idToken: String,
        requestUri: String,
        postBody: String,
        returnIdpCredential: Boolean = true
    ): FirebaseAccountsService =
        authenticateUser(
            api.signInWithIdp(
                SignInWithIdpRequest(idToken, requestUri, postBody, returnIdpCredential = returnIdpCredential),
            ),
        )

    public suspend fun signInAnonymously(): FirebaseAccountsService =
        authenticateUser(api.signUp(SignRequest()))

    public suspend fun signUp(email: String, password: String): FirebaseAccountsService =
        authenticateUser(api.signUp(SignRequest(email, password)))

    public suspend fun authenticateUser(initialToken: BearerToken): FirebaseAccountsService {
        var token = initialToken
        return FirebaseAccountsService(
            baseUrl,
            httpClient.bearer(
                loadTokens = { token },
                refreshToken = {
                    api.getToken(
                        TokenRequest(token.refreshToken!!),
                    ).also { newToken -> token = newToken }
                },
            ),
            apiKey,
            token.accessToken,
        )
    }
}
