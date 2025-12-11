package klib.data.net.http.client

import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.providers.BasicAuthConfig
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerAuthConfig
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.DigestAuthConfig
import io.ktor.client.plugins.auth.providers.DigestAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.auth.providers.digest
import klib.data.auth.model.BearerToken

public fun AuthConfig.basic(
    credentials: suspend () -> BasicAuthCredentials?,
    block: BasicAuthConfig.() -> Unit = {},
): Unit = basic {
    credentials {
        credentials()?.let { credentials ->
            BasicAuthCredentials(credentials.username, credentials.password)
        }
    }

    block()
}

public fun AuthConfig.digest(
    credentials: suspend () -> DigestAuthCredentials?,
    block: DigestAuthConfig.() -> Unit = {},
): Unit = digest {
    credentials {
        credentials()?.let { credentials ->
            DigestAuthCredentials(credentials.username, credentials.password)
        }
    }

    block()
}

public fun AuthConfig.bearer(
    loadTokens: suspend () -> BearerToken?,
    block: BearerAuthConfig.() -> Unit = {},
): Unit = bearer {
    loadTokens {
        loadTokens()?.let { token ->
            BearerTokens(token.accessToken, token.refreshToken)
        }
    }

    block()
}

public fun AuthConfig.bearer(
    loadTokens: suspend () -> BearerToken?,
    refreshToken: suspend () -> BearerToken,
    invalidRefreshToken: suspend () -> Unit = {},
    block: BearerAuthConfig.() -> Unit = {},
): Unit = bearer(loadTokens) {
    refreshTokens {
        try {
            refreshToken().let { token ->
                BearerTokens(token.accessToken, token.refreshToken ?: oldTokens?.refreshToken)
            }
        }
        catch (_: Throwable) {
            null
        }.also { token ->
            // Handle invalid refresh token.
            if (token == null) invalidRefreshToken()
        }
    }

    block()
}
