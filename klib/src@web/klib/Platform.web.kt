@file:OptIn(ExperimentalWasmJsInterop::class)

package klib

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@Suppress("MaxLineLength")
public val IS_BROWSER: Boolean = js(
    "typeof window !== 'undefined' && typeof window.document !== 'undefined' || typeof self !== 'undefined' && typeof self.location !== 'undefined'",
)

public val IS_NODE: Boolean = js(
    "typeof process !== 'undefined' && process.versions != null && process.versions.node != null",
)

public actual fun getPlatform(): Platform =
    when {
        IS_BROWSER -> BrowserPlatform("Web")
        IS_NODE -> NodePlatform("Web")
        else -> throw UnsupportedOperationException("Unsupported JS runtime")
    }

