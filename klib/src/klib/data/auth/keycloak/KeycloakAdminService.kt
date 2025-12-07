package klib.data.auth.keycloak

import io.ktor.client.HttpClient
import klib.data.auth.keycloak.model.ExecuteActionsEmail
import klib.data.auth.keycloak.model.ResetPassword
import klib.data.auth.keycloak.model.UserInfo
import klib.data.auth.keycloak.model.UserRepresentation
import klib.data.auth.keycloak.model.toUser
import klib.data.auth.keycloak.model.toUserRepresentation
import klib.data.auth.model.identity.User
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.ktorfit
import klib.data.type.serialization.json.encodeAnyToString
import klib.data.type.serialization.json.encodeToAny

public open class KeycloakAdminService(
    baseUrl: String,
    httpClient: HttpClient,
    public val realm: String,
) {

    private val api = httpClient.ktorfit { baseUrl(baseUrl) }.createKeycloakAdminApi()

    public suspend fun getUser(): User? {
        val userId = getUserInfo().sub
        return getUserRepresentations(UserRepresentation(id = userId), true)
            .singleOrNull()
            ?.let { userRepresentation ->
                val roles = api.getUserRealmRoles(realm, userId)
                userRepresentation.toUser().copy(roles = roles.map { role -> role.name!! }.toSet())
            }
    }

    public suspend fun resetPassword(newPassword: String): Unit =
        api.resetPassword(realm, getUserInfo().sub, ResetPassword(newPassword))

    public suspend fun executeActionsEmail(executeActionsEmail: ExecuteActionsEmail): Unit =
        api.updatePassword(realm, getUserInfo().sub, executeActionsEmail)

    public suspend fun getUsers(): Set<User> =
        getUserRepresentations().map(UserRepresentation::toUser).toSet()

    public suspend fun createUsers(users: Set<User>): Unit =
        users.forEach { user -> api.createUser(realm, user.toUserRepresentation()) }

    public suspend fun updateUsers(users: Set<User>): Unit =
        users.forEach { user ->
            val userId = getUserRepresentations(UserRepresentation(username = user.username), true).single().id!!
            api.updateUser(realm, userId, user.toUserRepresentation(userId))
        }

    public suspend fun deleteUsers(usernames: Set<String>): Unit =
        usernames.forEach { username ->
            val userId = getUserRepresentations(UserRepresentation(username = username), true).single().id!!
            api.deleteUser(realm, userId)
        }

    private suspend fun getUserInfo(): UserInfo = api.getUserInfo(realm)

    private suspend fun getUserRepresentations(userRepresentation: UserRepresentation? = null, exact: Boolean? = null): Set<UserRepresentation> =
        api.getUsers(
            realm,
            userRepresentation?.let {
                listOfNotNull(
                    exact?.let { "exact" to it.toString() },
                    (it.attributes as Map<*, *>?)?.let {
                        "q" to it.entries.joinToString(" ") { (k, v) -> "$k:${HTTP_CLIENT_JSON.encodeAnyToString(v)}" }
                    },
                ) + (HTTP_CLIENT_JSON.encodeToAny(it) as Map<*, *>).filter { (k, v) -> k !== "attributes" && v != null }
                    .map { (k, v) ->
                        k.toString() to HTTP_CLIENT_JSON.encodeAnyToString(v)
                    }
            }?.toMap().orEmpty(),
        )
}
