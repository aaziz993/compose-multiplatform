@file:OptIn(ExperimentalWasmJsInterop::class)

package permission

import js.objects.JsPlainObject
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.js

@JsPlainObject
public external interface Permissions : JsAny {

    public val permissions: Array<String>
}

@Suppress("UNCHECKED_CAST")
public fun permissions(): Promise<Permissions> = js("browser.permissions.getAll()") as Promise<Permissions>
