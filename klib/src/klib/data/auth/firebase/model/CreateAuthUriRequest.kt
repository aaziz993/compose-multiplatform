package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class CreateAuthUriRequest(
    val identifier: String, // User's email address
    val continueUri: String, // The URI to which the IDP redirects the user back. For this use case, this is just the current URL.
)
