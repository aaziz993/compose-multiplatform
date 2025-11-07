package klib.data.type.primitives.string.encoding

import diglol.encoding.decodeHex
import diglol.encoding.decodeHexToBytes
import diglol.encoding.encodeHex
import diglol.encoding.encodeHexToString

public actual fun ByteArray.encodeHex(): ByteArray = encodeHex()
public actual fun ByteArray.decodeHex(): ByteArray? = decodeHex()

public actual fun ByteArray.encodeHexToString(): String = encodeHexToString()
public actual fun String.decodeHexToBytes(): ByteArray? = decodeHexToBytes()
