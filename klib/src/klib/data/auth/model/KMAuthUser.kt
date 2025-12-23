package klib.data.auth.model

import com.sunildhiman90.kmauth.core.KMAuthUser

public fun KMAuthUser.toUser(): User = User(
    username = name,
    email = email,
    phone = phoneNumber,
    imageUrl = profilePicUrl,
)
