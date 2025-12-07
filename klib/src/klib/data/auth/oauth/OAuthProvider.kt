package klib.data.auth.oauth

import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger
import klib.data.auth.model.BearerToken
import klib.data.auth.oauth.model.AuthenticationFailedCause
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

private val Logger: Logger = KtorSimpleLogger("io.ktor.auth.oauth")

public abstract class OAuthProvider<T : BearerToken> {

    public abstract val onRedirectAuthenticate: suspend (url: Url) -> Unit

    protected var continuation: CancellableContinuation<T>? = null

    protected abstract suspend fun getRedirectUrl(): Url

    public suspend fun requestToken(): T {
        onRedirectAuthenticate(getRedirectUrl())

        val accessToken = suspendCancellableCoroutine { continuation ->
            this@OAuthProvider.continuation = continuation
        }

        continuation = null

        return accessToken
    }

    public abstract suspend fun callback(parameters: Parameters): AuthenticationFailedCause?

    protected fun resume(token: T) {
        continuation?.resume(token) { cause, _, _ -> Logger.warn("Callback canceled", cause) }
    }
}
