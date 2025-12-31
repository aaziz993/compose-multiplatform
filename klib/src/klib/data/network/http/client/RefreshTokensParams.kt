package klib.data.network.http.client

import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import klib.auth.model.BearerToken
import kotlinx.serialization.Serializable

public suspend fun RefreshTokensParams.refreshToken(
    url: String,
    clientId: String
): BearerToken = client.submitForm(
    url,
    parameters {
        append("grant_type", "refresh_token")
        append("client_id", clientId)
        append("refresh_token", requireNotNull(oldTokens?.refreshToken))
    },
) { markAsRefreshTokenRequest() }.body<BearerTokenImpl>()

@Serializable
private data class BearerTokenImpl(
    override val accessToken: String,
    override val refreshToken: String?,
) : BearerToken
