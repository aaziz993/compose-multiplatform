package klib.data.auth.firebase.client.admin.model

import kotlinx.serialization.Serializable

@Serializable
public data class SendOobRequest(
    val requestType: OobRequest,
    val email: String,
    val returnOobLink: Boolean = true,
)
