package klib.data.type.primitives.string.encoding

import diglol.encoding.decodeBase64
import diglol.encoding.decodeBase64ToBytes
import diglol.encoding.decodeBase64Url
import diglol.encoding.decodeBase64UrlToBytes
import diglol.encoding.encodeBase64
import diglol.encoding.encodeBase64ToString
import diglol.encoding.encodeBase64Url
import diglol.encoding.encodeBase64UrlToString

// Base64 Std
public actual fun ByteArray.encodeBase64(): ByteArray = encodeBase64()
public actual fun ByteArray.decodeBase64(): ByteArray? = decodeBase64()

public actual fun ByteArray.encodeBase64ToString(): String = encodeBase64ToString()
public actual fun String.decodeBase64ToBytes(): ByteArray? = decodeBase64ToBytes()

// Base64 Url
public actual fun ByteArray.encodeBase64Url(): ByteArray = encodeBase64Url()
public actual fun ByteArray.decodeBase64Url(): ByteArray? = decodeBase64Url()

public actual fun ByteArray.encodeBase64UrlToString(): String = encodeBase64UrlToString()
public actual fun String.decodeBase64UrlToBytes(): ByteArray? = decodeBase64UrlToBytes()
