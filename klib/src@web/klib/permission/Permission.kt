@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.permission

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsArray
import kotlin.js.Promise
import kotlin.js.js
import web.permissions.PermissionName

public external interface Permissions : JsAny {

    public val permissions: JsArray<PermissionName>
}

public fun permissions(): Promise<Permissions> = js("browser.permissions.getAll()")
