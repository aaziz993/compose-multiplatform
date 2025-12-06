package klib.data.auth.firebase

import io.ktor.client.*
import klib.data.auth.client.bearer.AbstractBearerAuthProvider
import klib.data.auth.client.bearer.model.BearerToken
import klib.data.auth.client.model.BearerToken
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

    public suspend fun getUser(): User? = adminClient.lookup("")?.asUser

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

    public suspend fun deleteUsers(usernames: Set<String>, password: String) {
        usernames.forEach { adminClient.delete() }
    }

    public suspend fun resetPassword(password: String, newPassword: String) {
        TODO("Not yet implemented")
    }

    public suspend fun forgotPassword() {
        adminClient.sendOdbCode()
    }
}
