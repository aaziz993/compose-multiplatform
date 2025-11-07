package klib.data.type.primitives.string.encoding

// Base64 Std
public  expect fun ByteArray.encodeBase64(): ByteArray
public  expect fun ByteArray.decodeBase64(): ByteArray?

public  expect fun ByteArray.encodeBase64ToString(): String
public  expect fun String.decodeBase64ToBytes(): ByteArray?

// Base64 Url
public  expect fun ByteArray.encodeBase64Url(): ByteArray
public  expect fun ByteArray.decodeBase64Url(): ByteArray?

public  expect fun ByteArray.encodeBase64UrlToString(): String
public  expect fun String.decodeBase64UrlToBytes(): ByteArray?
