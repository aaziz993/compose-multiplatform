package klib.data.type.primitives.string

public actual fun CharSequence.lengthSequence(length: Int): CharSequence {
    if (this is StringBuilder) {
        setLength(length)
        return this
    }
    return subSequence(0, length)
}
