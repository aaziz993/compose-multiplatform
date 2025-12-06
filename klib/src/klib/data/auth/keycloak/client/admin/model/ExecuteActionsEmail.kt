package klib.data.auth.keycloak.client.admin.model

import kotlinx.serialization.Serializable

@Serializable
public data class ExecuteActionsEmail(
    val actions: List<EmailAction>,
    val redirectUri: String? = null
)
