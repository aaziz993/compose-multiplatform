package clib.presentation.components.image.avatar.model

import kotlinx.serialization.Serializable

@Serializable
public data class AvatarView(
    val resetPassword: String = "Reset password",
    val signOut: String = "Sign out",
)
