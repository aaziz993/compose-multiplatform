package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class BatchDeleteResponse(
    val errors: List<ErrorInfo>,
)
