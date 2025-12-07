package klib.data.auth.firebase

import io.ktor.client.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.parameters
import klib.data.auth.firebase.model.BatchDeleteRequest
import klib.data.auth.firebase.model.BatchDeleteResponse
import klib.data.auth.firebase.model.CreateAuthUriRequest
import klib.data.auth.firebase.model.CreateAuthUriResponse
import klib.data.auth.firebase.model.DeleteRequest
import klib.data.auth.firebase.model.LookupRequest
import klib.data.auth.firebase.model.LookupResponse
import klib.data.auth.firebase.model.OobRequest
import klib.data.auth.firebase.model.ResetPasswordRequest
import klib.data.auth.firebase.model.ResetPasswordResponse
import klib.data.auth.firebase.model.SendOobRequest
import klib.data.auth.firebase.model.SendOobResponse
import klib.data.auth.firebase.model.SignInResponse
import klib.data.auth.firebase.model.SignInWithCustomTokenRequest
import klib.data.auth.firebase.model.SignInWithCustomTokenResponse
import klib.data.auth.firebase.model.SignInWithIdpRequest
import klib.data.auth.firebase.model.SignInWithIdpResponse
import klib.data.auth.firebase.model.SignRequest
import klib.data.auth.firebase.model.SignUpResponse
import klib.data.auth.firebase.model.TokenRequest
import klib.data.auth.firebase.model.TokenResponse
import klib.data.auth.firebase.model.UpdateRequest
import klib.data.auth.firebase.model.UpdateResponse
import klib.data.auth.firebase.model.UserInfo
import klib.data.auth.model.identity.User
import klib.data.net.http.client.ktorfit
import klib.data.type.serialization.serializers.collections.SerializableAnyMap

public class FirebaseAdminService(
    baseUrl: String,
    httpClient: HttpClient,
    public val apiKey: String,
) {

    private val api = httpClient.config {
        defaultRequest {
            parameters {
                append("apiKey", apiKey)
            }
        }
    }.ktorfit { baseUrl(baseUrl) }.createFirebaseAdminApi()


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
        customClaims: SerializableAnyMap? = null,
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



    public suspend fun getUser(): User? = lookup("")?.asUser

    public suspend fun getUsers(): Set<User> =
        adminClient.batchGet().toList().flatten().map { it.asUser }.toSet()

    public suspend fun createUsers(users: Set<User>, password: String): Unit =
        users.forEach {
            adminClient.create(
                ""
                    it . username !!,
                password,
            )
        }

    public suspend fun updateUsers(users: Set<User>, password: String) {
        users.forEach { adminClient.update() }
    }

    public suspend fun deleteUsers(usernames: Set<String>) {
        usernames.forEach {username->

            api.delete(DeleteRequest(localId))
        }
    }

    public companion object Companion {

        public const val END_OF_LIST: String = "";
    }
}
