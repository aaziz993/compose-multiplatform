package klib.data.type

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.asJsException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalWasmJsInterop::class)
public suspend fun <T : JsAny?> Promise<T>.await(): T = suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
    this@await.then<JsAny?>(
        onFulfilled = {
            cont.resume(it)
            null
        },
        onRejected = {
            cont.resumeWithException(it.asJsException())
            null
        },
    )
}
