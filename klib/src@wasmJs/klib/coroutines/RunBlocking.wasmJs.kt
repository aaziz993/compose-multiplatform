package klib.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalWasmJsInterop::class)
public actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T,
): T = GlobalScope.promise(context) { block() } as T
