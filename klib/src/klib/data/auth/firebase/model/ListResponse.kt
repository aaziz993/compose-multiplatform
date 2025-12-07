package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class ListResponse(
    val users: List<UserRecord>,
    val nextPageToken: String
)
