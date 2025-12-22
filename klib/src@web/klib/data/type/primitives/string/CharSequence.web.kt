package klib.data.type.primitives.string

public actual fun CharSequence.lengthSequence(length: Int): CharSequence = subSequence(0, length)
