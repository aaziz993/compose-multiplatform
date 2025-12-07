package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class ErrorInfo(
    val index: Int,
    val message: String,
    val localId: String?,
)
