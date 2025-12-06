package klib.data.auth.firebase.client.admin

import io.ktor.client.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.parameters
import klib.data.auth.firebase.client.admin.model.BatchDeleteRequest
import klib.data.auth.firebase.client.admin.model.BatchDeleteResponse
import klib.data.auth.firebase.client.admin.model.CreateAuthUriRequest
import klib.data.auth.firebase.client.admin.model.CreateAuthUriResponse
import klib.data.auth.firebase.client.admin.model.DeleteRequest
import klib.data.auth.firebase.client.admin.model.LookupRequest
import klib.data.auth.firebase.client.admin.model.LookupResponse
import klib.data.auth.firebase.client.admin.model.OobRequest
import klib.data.auth.firebase.client.admin.model.ResetPasswordRequest
import klib.data.auth.firebase.client.admin.model.ResetPasswordResponse
import klib.data.auth.firebase.client.admin.model.SendOobRequest
import klib.data.auth.firebase.client.admin.model.SendOobResponse
import klib.data.auth.firebase.client.admin.model.SignInResponse
import klib.data.auth.firebase.client.admin.model.SignInWithCustomTokenRequest
import klib.data.auth.firebase.client.admin.model.SignInWithCustomTokenResponse
import klib.data.auth.firebase.client.admin.model.SignInWithIdpRequest
import klib.data.auth.firebase.client.admin.model.SignInWithIdpResponse
import klib.data.auth.firebase.client.admin.model.SignRequest
import klib.data.auth.firebase.client.admin.model.SignUpResponse
import klib.data.auth.firebase.client.admin.model.TokenRequest
import klib.data.auth.firebase.client.admin.model.TokenResponse
import klib.data.auth.firebase.client.admin.model.UpdateRequest
import klib.data.auth.firebase.client.admin.model.UpdateResponse
import klib.data.auth.firebase.client.admin.model.UserInfo
import klib.data.net.http.client.KtorfitClient
import kotlinx.serialization.json.JsonObject

public class FirebaseAdminClient(
    baseUrl: String,
    httpClient: HttpClient,
    public val apiKey: String,
) : KtorfitClient(baseUrl, httpClient) {

    private val api = ktorfit.createFirebaseAdminApi()

    override fun DefaultRequest.DefaultRequestBuilder.configureDefaultRequest() {
        parameters {
            append("apiKey", apiKey)
        }
    }

    public suspend fun signInWithCustomToken(token: String): SignInWithCustomTokenResponse =
        api.signInWithCustomToken(SignInWithCustomTokenRequest(token))

    public suspend fun getToken(refreshToken: String): TokenResponse =
        api.getToken(TokenRequest(refreshToken = refreshToken))

    public suspend fun signUp(email: String, password: String): SignUpResponse =
        api.signUp(SignRequest(email, password))

    public suspend fun signInWithPassword(email: String, password: String): SignInResponse =
        api.signInWithPassword(SignRequest(email, password))

    public suspend fun signInAnonymously(): SignUpResponse =
        api.signUp(SignRequest())

    public suspend fun signInWithIdp(
        idToken: String,
        requestUri: String,
        postBody: String,
        returnIdpCredential: Boolean = true
    ): SignInWithIdpResponse =
        api.signInWithIdp(SignInWithIdpRequest(idToken, requestUri, postBody, returnIdpCredential = returnIdpCredential))

    public suspend fun lookup(idToken: String): LookupResponse =
        api.lookup(LookupRequest(idToken))

    public suspend fun update(
        localId: String? = null,
        oobCode: String? = null,
        email: String? = null,
        passwordHash: String? = null,
        providerUserInfo: UserInfo? = null,
        idToken: String? = null,
        refreshToken: String? = null,
        expiresIn: String? = null,
        phoneNumber: String? = null,
        emailVerified: Boolean? = null,
        displayName: String? = null,
        photoUrl: String? = null,
        disabled: Boolean? = null,
        password: String? = null,
        customClaims: JsonObject? = null,
        deleteProvider: List<String>? = null,
        idSince: Long? = null,
        returnSecureToken: String? = null,
    ): UpdateResponse = api.update(
        UpdateRequest(
            localId,
            oobCode,
            email,
            passwordHash,
            providerUserInfo,
            idToken,
            refreshToken,
            expiresIn,
            phoneNumber,
            emailVerified,
            displayName,
            photoUrl,
            disabled,
            password,
            customClaims,
            deleteProvider,
            idSince,
            returnSecureToken,
        ),
    )

    public suspend fun delete(localId: String): Unit =
        api.delete(DeleteRequest(localId))

    public suspend fun batchDelete(localIds: List<String>, force: Boolean = true): BatchDeleteResponse =
        api.batchDelete(BatchDeleteRequest(localIds, force))

    public suspend fun createAuthUri(
        identifier: String,
        continueUri: String,
    ): CreateAuthUriResponse =
        api.createAuthUri(
            CreateAuthUriRequest(
                identifier,
                continueUri,
            ),
        )

    public suspend fun sendOdbCode(
        requestType: OobRequest,
        email: String,
    ): SendOobResponse = api.sendOobCode(SendOobRequest(requestType, email))

    public suspend fun resetPassword(
        oobCode: String,
        newPassword: String? = null
    ): ResetPasswordResponse = api.resetPassword(ResetPasswordRequest(oobCode, newPassword))

    public companion object {

        public const val END_OF_LIST: String = "";
    }
}
