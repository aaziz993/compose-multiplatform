package klib.data.type.function

public fun <T, R> func1(block: (T) -> R): (T) -> R = block
