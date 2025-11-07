package klib.data.type.primitives.string.encoding

// Base32 Std
public expect fun ByteArray.encodeBase32(): ByteArray
public expect fun ByteArray.decodeBase32(): ByteArray?
public expect fun ByteArray.encodeBase32ToString(): String
public expect fun String.decodeBase32ToBytes(): ByteArray?

// Base32 Hex
public expect fun ByteArray.encodeBase32Hex(): ByteArray
public expect fun ByteArray.decodeBase32Hex(): ByteArray?
public expect fun ByteArray.encodeBase32HexToString(): String
public expect fun String.decodeBase32HexToBytes(): ByteArray?
