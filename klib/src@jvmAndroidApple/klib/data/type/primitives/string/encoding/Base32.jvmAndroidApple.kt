package klib.data.type.primitives.string.encoding

import diglol.encoding.decodeBase32
import diglol.encoding.decodeBase32Hex
import diglol.encoding.decodeBase32HexToBytes
import diglol.encoding.decodeBase32ToBytes
import diglol.encoding.encodeBase32
import diglol.encoding.encodeBase32Hex
import diglol.encoding.encodeBase32HexToString
import diglol.encoding.encodeBase32ToString

// Base32 Std
public actual fun ByteArray.encodeBase32(): ByteArray = encodeBase32()
public actual fun ByteArray.decodeBase32(): ByteArray? = decodeBase32()
public actual fun ByteArray.encodeBase32ToString(): String = encodeBase32ToString()
public actual fun String.decodeBase32ToBytes(): ByteArray? = decodeBase32ToBytes()

// Base32 Hex
public actual fun ByteArray.encodeBase32Hex(): ByteArray = encodeBase32Hex()
public actual fun ByteArray.decodeBase32Hex(): ByteArray? = decodeBase32Hex()
public actual fun ByteArray.encodeBase32HexToString(): String = encodeBase32HexToString()
public actual fun String.decodeBase32HexToBytes(): ByteArray? = decodeBase32HexToBytes()
