package klib.data.backup.onedrive.model

import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class OneDriveOAuth2RefreshableTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresInSeconds: Int,
    @SerialName("token_type") val tokenType: TokenType,
    @SerialName("refresh_token") val refreshToken: String,
) {

    @Serializable
    enum class TokenType {

        Bearer
    }
}

internal fun OneDriveOAuth2RefreshableTokenResponse.toOneDriveRefreshableAccessData(): OneDriveRefreshableAccessData =
    OneDriveRefreshableAccessData(
        expiresAt = Clock.System.now() + expiresInSeconds.seconds / 2,
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
