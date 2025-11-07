package klib.data.type.primitives.string.encoding

public expect fun ByteArray.encodeHex(): ByteArray
public expect fun ByteArray.decodeHex(): ByteArray?

public expect fun ByteArray.encodeHexToString(): String
public expect fun String.decodeHexToBytes(): ByteArray?
