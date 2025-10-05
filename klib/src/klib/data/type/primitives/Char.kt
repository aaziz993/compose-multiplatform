package klib.data.type.primitives

@Suppress("SameReturnValue")
public val Char.Companion.DEFAULT: Char
    get() = ' '

public val HEX_DIGIT_CHARS: CharArray =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
