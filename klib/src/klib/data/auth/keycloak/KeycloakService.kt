package klib.data.auth.keycloak

import io.ktor.client.HttpClient
import klib.data.auth.client.oauth.AbstractOAuth2Provider
import klib.data.auth.client.oauth.model.OAuthAccessTokenResponse
import klib.data.auth.keycloak.client.admin.KeycloakAdminClient
import klib.data.auth.keycloak.client.admin.model.EmailAction
import klib.data.auth.keycloak.client.admin.model.ExecuteActionsEmail
import klib.data.auth.keycloak.client.admin.model.ResetPassword
import klib.data.auth.keycloak.client.admin.model.UserInfo
import klib.data.auth.keycloak.client.admin.model.UserRepresentation
import klib.data.auth.keycloak.client.token.KeycloakTokenClient
import klib.data.auth.model.identity.User
import klib.data.cache.CoroutineCache

public class KeycloakService(
    name: String,
    baseUrl: String,
    httpClient: HttpClient,
    realm: String,
    clientId: String,
    cache: CoroutineCache<String, OAuthAccessTokenResponse.OAuth2>,
) : AbstractOAuth2Provider(
    name,
    baseUrl,
    httpClient,
    "$baseUrl/token",
    clientId,
    "realms/{$realm}/protocol/openid-connect/token",
    cache,
    {},
) {

    private val tokenClient = KeycloakTokenClient(
        baseUrl,
        httpClient,
        realm,
        clientId,
    )

    private val adminClient = KeycloakAdminClient(
        baseUrl,
        this@KeycloakService.httpClient,
        realm,
    )

    public suspend fun getUser(): User? {
        val userId = adminClient.getUserInfo().let(UserInfo::sub)

        return adminClient
            .getUsers(UserRepresentation(id = userId), true)
            .singleOrNull()
            ?.let { userRepresentation ->
                val roles = adminClient.getUserRealmRoles(userId)
                userRepresentation.copy(realmRoles = roles.map { role -> role.name!! }.toSet()).asUser
            }
    }

    public suspend fun getUsers(): Set<User> =
        adminClient.getUsers().map(UserRepresentation::asUser).toSet()

    public suspend fun createUsers(users: Set<User>, password: String): Unit =
        users.forEach { adminClient.createUser(UserRepresentation(it)) }

    public suspend fun updateUsers(users: Set<User>, password: String): Unit =
        users.forEach { user ->
            val userId = adminClient
                .getUsers(UserRepresentation(username = user.username), true)
                .single()
                .let(UserRepresentation::id)

            adminClient.updateUser(UserRepresentation(user, userId))
        }

    public suspend fun deleteUsers(usernames: Set<String>, password: String): Unit =
        usernames.forEach {
            val userId = adminClient
                .getUsers(UserRepresentation(username = it), true)
                .single()
                .let(UserRepresentation::id)!!

            adminClient.deleteUser(userId)
        }

    public suspend fun resetPassword(password: String, newPassword: String) {
        val userId = adminClient.getUserInfo().let(UserInfo::sub)

        adminClient.resetPassword(userId, ResetPassword(newPassword))
    }

    public suspend fun forgotPassword(): Unit =
        executeActionsEmail(ExecuteActionsEmail(listOf(EmailAction.UPDATE_PASSWORD)))

    public suspend fun executeActionsEmail(executeActionsEmail: ExecuteActionsEmail) {
        val userId = adminClient.getUserInfo().let(UserInfo::sub)

        adminClient.executeActionsEmail(userId, executeActionsEmail)
    }
}
