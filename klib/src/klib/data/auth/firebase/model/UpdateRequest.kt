package klib.data.auth.firebase.model

import klib.data.type.serialization.serializers.collections.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
public data class UpdateRequest(
    val localId: String? = null,
    val oobCode: String? = null,
    val email: String? = null,
    val passwordHash: String? = null,
    val providerUserInfo: UserInfo? = null,
    val idToken: String? = null,
    val refreshToken: String? = null,
    val expiresIn: String? = null,
    val phoneNumber: String? = null,
    val emailVerified: Boolean? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val disabled: Boolean? = null,
    val password: String? = null,
    val customClaims: SerializableAnyMap? = null,
    val deleteProvider: List<String>? = null,
    val validSince: Long? = null,
    val returnSecureToken: String? = null,
)
