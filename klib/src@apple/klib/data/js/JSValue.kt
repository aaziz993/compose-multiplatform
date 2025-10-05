package klib.data.js

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import platform.JavaScriptCore.JSValue

public fun JSValue.then(onFulfilled: (Any?) -> Unit = {}, onRejected: (Any?) -> Any? = {}): JSValue =
    invokeMethod("then", listOf(onFulfilled, onRejected))!!

public fun JSValue.catch(onError: (Any?) -> Any?): JSValue =
    invokeMethod("catch", listOf(onError))!!

public suspend fun JSValue.await(): Any? = suspendCoroutine { continuation ->
    then(continuation::resume) { value ->
        continuation.resumeWithException(Exception("Promise rejected: $value"))
    }
}
