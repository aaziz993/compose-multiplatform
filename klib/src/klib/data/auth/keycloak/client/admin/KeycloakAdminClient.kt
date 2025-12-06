package klib.data.auth.keycloak.client.admin

import klib.data.auth.keycloak.client.admin.model.ExecuteActionsEmail
import klib.data.auth.keycloak.client.admin.model.ResetPassword
import klib.data.auth.keycloak.client.admin.model.RoleRepresentation
import klib.data.auth.keycloak.client.admin.model.UserInfo
import klib.data.auth.keycloak.client.admin.model.UserRepresentation
import io.ktor.client.*
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.ktorfit
import klib.data.type.serialization.json.encodeAnyToString
import klib.data.type.serialization.json.encodeToAny

public open class KeycloakAdminClient(
    baseUrl: String,
    httpClient: HttpClient,
    public val realm: String
) {

    private val api = httpClient.ktorfit { baseUrl(baseUrl) }.createKeycloakAdminApi()

    public suspend fun createUser(userRepresentation: UserRepresentation): Unit =
        api.createUser(realm, userRepresentation)

    public suspend fun getUsers(userRepresentation: UserRepresentation? = null, exact: Boolean? = null): Set<UserRepresentation> =
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

    public suspend fun updateUser(userRepresentation: UserRepresentation): Unit =
        api.updateUser(realm, userRepresentation.id!!, userRepresentation)

    public suspend fun deleteUser(userId: String): Unit = api.deleteUser(realm, userId)

    public suspend fun getUserInfo(): UserInfo = api.getUserInfo(realm)

    public suspend fun getUserRealmRoles(userId: String): Set<RoleRepresentation> =
        api.getUserRealmRoles(realm, userId)

    public suspend fun resetPassword(userId: String, resetPassword: ResetPassword): Unit =
        api.resetPassword(realm, userId, resetPassword)

    public suspend fun executeActionsEmail(userId: String, executeActionsEmail: ExecuteActionsEmail): Unit =
        api.updatePassword(realm, userId, executeActionsEmail)
}
