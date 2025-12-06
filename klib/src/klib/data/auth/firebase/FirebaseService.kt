package klib.data.auth.firebase

import io.ktor.client.*
import klib.data.auth.client.bearer.AbstractBearerAuthProvider
import klib.data.auth.client.bearer.model.BearerToken
import klib.data.auth.firebase.client.admin.FirebaseAdminClient
import klib.data.auth.firebase.client.admin.model.TokenResponse
import klib.data.auth.model.identity.User
import klib.data.cache.CoroutineCache

public class FirebaseService(
    name: String,
    httpClient: HttpClient,
    address: String,
    apiKey: String,
   cache: CoroutineCache<String, TokenResponse>,
) : AbstractBearerAuthProvider(
    name,
    httpClient,
    address,
    "token",
    cache,
) {

    private val adminClient = FirebaseAdminClient(
        httpClient,
        address,
        apiKey,
    )

    override suspend fun getToken(username: String, password: String): BearerToken =
        adminClient.signInWithPassword(username, password)

    override suspend fun getTokenByRefreshToken(refreshToken: String): BearerToken =
        adminClient.getToken(refreshToken)

    override suspend fun getUser(): User? = adminClient.lookup("")?.asUser

    override suspend fun getUsers(): Set<User> =
        adminClient.batchGet().toList().flatten().map { it.asUser }.toSet()

    override suspend fun createUsers(users: Set<User>, password: String): Unit =
        users.forEach {
            adminClient.create(
                ""
                    it . username !!,
                password,
            )
        }

    override suspend fun updateUsers(users: Set<User>, password: String) {
        users.forEach { adminClient.update() }
    }

    override suspend fun deleteUsers(usernames: Set<String>, password: String) {
        usernames.forEach { adminClient.delete() }
    }

    override suspend fun resetPassword(password: String, newPassword: String) {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword() {
        adminClient.sendOdbCode()
    }
}
