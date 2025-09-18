package klib.data.type.functions

public fun interface Merger<E> {
    public fun merge(o1: E, o2: E): E

    public companion object {
        public fun <E> default(): Merger<E> = Merger { o1, o2 -> o1 ?: o2 }
    }
}
