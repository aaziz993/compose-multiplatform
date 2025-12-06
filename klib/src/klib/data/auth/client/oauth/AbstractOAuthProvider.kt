package klib.data.auth.client.oauth

import io.ktor.client.HttpClient
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger
import klib.data.auth.client.model.BearerToken
import klib.data.auth.client.oauth.model.AuthenticationFailedCause
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

private val Logger: Logger = KtorSimpleLogger("io.ktor.auth.oauth")

public abstract class AbstractOAuthProvider<T : BearerToken>(
    public val name: String? = null,
    protected val httpClient: HttpClient,
    public val callbackRedirectUrl: String,
    protected val onRedirectAuthenticate: suspend (url: Url) -> Unit,
) {

    protected var continuation: CancellableContinuation<T>? = null

    protected abstract suspend fun getRedirectUrl(): Url

    protected suspend fun requestToken(): T {
        onRedirectAuthenticate(getRedirectUrl())

        val accessToken = suspendCancellableCoroutine { continuation ->
            this@AbstractOAuthProvider.continuation = continuation
        }

        continuation = null

        return accessToken
    }

    public abstract suspend fun callback(parameters: Parameters): AuthenticationFailedCause?

    protected fun resume(token: T) {
        continuation?.resume(token) { cause, _, _ -> Logger.warn("Callback canceled", cause) }
    }
}
