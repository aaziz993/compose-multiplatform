package klib.data.auth.keycloak

import io.ktor.client.HttpClient
import klib.data.auth.firebase.client.admin.model.TokenResponse
import klib.data.auth.keycloak.client.admin.KeycloakAdminClient
import klib.data.auth.keycloak.client.admin.model.EmailAction
import klib.data.auth.keycloak.client.admin.model.ExecuteActionsEmail
import klib.data.auth.keycloak.client.admin.model.ResetPassword
import klib.data.auth.keycloak.client.admin.model.UserInfo
import klib.data.auth.keycloak.client.admin.model.UserRepresentation
import klib.data.auth.model.identity.User
import klib.data.net.http.client.bearer

public class KeycloakService(
    baseUrl: String,
    httpClient: HttpClient,
    realm: String,
    token: TokenResponse,
) {

    private val adminClient = KeycloakAdminClient(
        baseUrl,
        httpClient.bearer(
            { token },
        ),
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

    public suspend fun createUsers(users: Set<User>): Unit =
        users.forEach { adminClient.createUser(UserRepresentation(it)) }

    public suspend fun updateUsers(users: Set<User>): Unit =
        users.forEach { user ->
            val userId = adminClient
                .getUsers(UserRepresentation(username = user.username), true)
                .single()
                .let(UserRepresentation::id)

            adminClient.updateUser(UserRepresentation(user, userId))
        }

    public suspend fun deleteUsers(usernames: Set<String>): Unit =
        usernames.forEach {
            val userId = adminClient
                .getUsers(UserRepresentation(username = it), true)
                .single()
                .let(UserRepresentation::id)!!

            adminClient.deleteUser(userId)
        }

    public suspend fun resetPassword(newPassword: String) {
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
