package klib.data.auth.firebase.client.admin.model

import kotlinx.serialization.Serializable

@Serializable
public data class BatchDeleteResponse(
    val errors: List<ErrorInfo>,
)
