package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class BatchDeleteRequest(
    val localIds: List<String>,
    val force: Boolean = true
)
