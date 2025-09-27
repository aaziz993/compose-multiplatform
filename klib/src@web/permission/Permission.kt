@file:OptIn(ExperimentalWasmJsInterop::class)
package permission

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.js

public external interface Permissions : JsAny {

    public val permissions: Array<String>
}

public fun permissions(): Promise<Permissions> = js("browser.permissions.getAll()") as Promise<Permissions>
