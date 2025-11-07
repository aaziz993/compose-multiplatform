package klib.data.type.primitives.string.encoding

import diglol.encoding.decodeBase45
import diglol.encoding.decodeBase45ToBytes
import diglol.encoding.encodeBase45ToString
import diglol.encoding.encodeBase45

public actual fun ByteArray.encodeBase45(): ByteArray = encodeBase45()
public actual fun ByteArray.decodeBase45(): ByteArray? = decodeBase45()

public actual fun ByteArray.encodeBase45ToString(): String = encodeBase45ToString()
public actual fun String.decodeBase45ToBytes(): ByteArray? = decodeBase45ToBytes()
