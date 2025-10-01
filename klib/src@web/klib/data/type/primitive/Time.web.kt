@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.type.primitive

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsModule

@JsModule("@js-joda/timezone")
public external object JsJodaTimeZoneModule

private val jsJodaTz = JsJodaTimeZoneModule
