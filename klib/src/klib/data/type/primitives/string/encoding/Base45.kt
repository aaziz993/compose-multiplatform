package klib.data.type.primitives.string.encoding

public  expect fun ByteArray.encodeBase45(): ByteArray
public  expect fun ByteArray.decodeBase45(): ByteArray?

public  expect fun ByteArray.encodeBase45ToString(): String
public  expect fun String.decodeBase45ToBytes(): ByteArray?
