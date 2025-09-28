@file:OptIn(ExperimentalWasmJsInterop::class)

package permission

import js.objects.JsPlainObject
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsArray
import kotlin.js.Promise
import kotlin.js.js
import web.permissions.PermissionName

@JsPlainObject
public external interface Permissions : JsAny {

    public val permissions: JsArray<PermissionName>
}

@Suppress("UNCHECKED_CAST")
public fun permissions(): Promise<Permissions> = js("browser.permissions.getAll()") as Promise<Permissions>
