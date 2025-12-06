package klib.data.backup.dropbox.model

import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class DropboxOAuth2TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresInSeconds: Int,
    @SerialName("token_type") val tokenType: TokenType,
) {

    @Serializable
    enum class TokenType {

        bearer
    }
}

internal fun DropboxOAuth2TokenResponse.toDropboxRefreshableAccessData(refreshToken: String): DropboxRefreshableAccessData =
    DropboxRefreshableAccessData(
        expiresAt = Clock.System.now() + expiresInSeconds.seconds / 2,
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
