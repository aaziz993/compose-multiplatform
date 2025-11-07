@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.type.primitives.string.encoding

import js.buffer.ArrayBuffer
import js.typedarrays.Uint8Array
import js.typedarrays.toByteArray
import js.typedarrays.toUint8Array
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.definedExternally

internal const val equalsByte = '='.code.toByte()

internal fun ByteArray.selfOrCopyOf(newSize: Int): ByteArray =
    if (size == newSize) this else copyOf(newSize)

// Was all padding, whitespace, or otherwise ignorable characters
internal fun ByteArray.sizeOfIgnoreTrailing(): Int {
    var limit = size
    while (limit > 0) {
        val c = this[limit - 1].toInt()
        if (c != '='.code && c != '\n'.code && c != '\r'.code && c != ' '.code && c != '\t'.code) {
            break
        }
        limit--
    }
    return limit
}

private external class TextEncoder(utfLabel: String = definedExternally) {

    fun encode(input: String): Uint8Array<ArrayBuffer>
}

private external class TextDecoder(utfLabel: String = definedExternally) {

    fun decode(input: Uint8Array<ArrayBuffer>): String
}

// Instantiate global encoder/decoder once
private val textEncoder: TextEncoder = TextEncoder("utf-8")
private val textDecoder: TextDecoder = TextDecoder("utf-8")

internal fun String.jsEncodeToByteArray(): ByteArray = textEncoder.encode(this).toByteArray()

internal fun ByteArray.jsDecodeToString(): String = textDecoder.decode(toUint8Array())
