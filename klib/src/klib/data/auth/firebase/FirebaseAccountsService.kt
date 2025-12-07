package klib.data.auth.firebase

import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.parameters
import klib.data.net.http.client.ktorfit

public class FirebaseAccountsService(
    baseUrl: String,
    httpClient: HttpClient,
    public val apiKey: String,
    private val idToken: String,
) {

    private val api = httpClient.config {
        defaultRequest {
            parameters {
                append("apiKey", apiKey)
            }
        }
    }.ktorfit { baseUrl(baseUrl) }.createFirebaseAccountsApi()

//    public suspend fun getUsers(): List<User> =
//        api.lookup(LookupRequest(idToken)).users!!.map { it.toUser() }
//
//    public suspend fun createUsers(users: Set<User>, password: String): Unit =
//        users.forEach { user ->
//
//            api.create(
//                CreateRequest(
//                    "",
//                    user.email.orEmpty(),
//                    user.email != null,
//                    user.phone.orEmpty(),
//
//                    ),
//            )
//        }
//
//    public suspend fun update(
//        localId: String? = null,
//        oobCode: String? = null,
//        email: String? = null,
//        passwordHash: String? = null,
//        providerUserInfo: UserInfo? = null,
//        idToken: String? = null,
//        refreshToken: String? = null,
//        expiresIn: String? = null,
//        phoneNumber: String? = null,
//        emailVerified: Boolean? = null,
//        displayName: String? = null,
//        photoUrl: String? = null,
//        disabled: Boolean? = null,
//        password: String? = null,
//        customClaims: SerializableAnyMap? = null,
//        deleteProvider: List<String>? = null,
//        idSince: Long? = null,
//        returnSecureToken: String? = null,
//    ): UpdateResponse = api.update(
//        UpdateRequest(
//            localId,
//            oobCode,
//            email,
//            passwordHash,
//            providerUserInfo,
//            idToken,
//            refreshToken,
//            expiresIn,
//            phoneNumber,
//            emailVerified,
//            displayName,
//            photoUrl,
//            disabled,
//            password,
//            customClaims,
//            deleteProvider,
//            idSince,
//            returnSecureToken,
//        ),
//    )
//
//    public suspend fun batchDelete(localIds: List<String>, force: Boolean = true): BatchDeleteResponse =
//        api.delete(BatchDeleteRequest(localIds, force))
//
//    public suspend fun createAuthUri(
//        identifier: String,
//        continueUri: String,
//    ): CreateAuthUriResponse =
//        api.createAuthUri(
//            CreateAuthUriRequest(
//                identifier,
//                continueUri,
//            ),
//        )
//
//    public suspend fun sendOdbCode(
//        requestType: OobRequest,
//        email: String,
//    ): SendOobResponse = api.sendOobCode(SendOobRequest(requestType, email))
//
//    public suspend fun resetPassword(
//        oobCode: String,
//        newPassword: String? = null
//    ): ResetPasswordResponse = api.resetPassword(ResetPasswordRequest(oobCode, newPassword))
//
//    public suspend fun updateUsers(users: Set<User>, password: String) {
//        users.forEach { adminClient.update() }
//    }
//
//    public suspend fun deleteUsers(usernames: Set<String>) {
//        usernames.forEach { username ->
//
//            api.delete(DeleteRequest(idToken))
//        }
//    }

    public companion object Companion {

        public const val END_OF_LIST: String = "";
    }
}
