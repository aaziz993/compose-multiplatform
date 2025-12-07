package klib.data.auth.firebase.model

import klib.data.auth.model.identity.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class UserRecord(
    val localId: String,
    val tenantId: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val emailVerified: Boolean,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val disabled: Boolean,
    val providerUserInfo: List<UserInfo>? = null,
    val providerId: String,
    val tokensValidAfterTimestamp: Long,
    val userMetadata: UserMetadata,
    val createdAt: Long,
    val lastLoginAt: Long,
    val lastRefreshAt: String? = null,
    val validSince: Long,
    val customClaims: JsonObject,
    val passwordUpdatedAt: Double? = null,
    val passwordHash: String? = null,
    val passwordSalt: String? = null,
    val customAuth: Boolean? = null,
    val updateRequest: UpdateRequest,
)

public fun UserRecord.toUser(): User = User(
    username = email,
    firstName = null,
    lastName = null,
    phone = phoneNumber,
    email = email,
    image = photoUrl,
    roles = null,
    attributes = null,
)
