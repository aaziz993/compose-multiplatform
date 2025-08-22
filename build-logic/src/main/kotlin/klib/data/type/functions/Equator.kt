package klib.data.type.functions

/**
 * An equation function, which determines equality between objects of type T.
 *
 *
 * It is the functional sibling of [java.util.Comparator]; [Equator] is to
 * [Object] as [java.util.Comparator] is to [java.lang.Comparable].
 *
 *
 * @param <T> the types of object this [Equator] can evaluate.
 * @since 4.0
</T> */
public fun interface Equator<T> {

    /**
     * Evaluates the two arguments for their equality.
     *
     * @param o1 the first object to be equated.
     * @param o2 the second object to be equated.
     * @return whether the two objects are equal.
     */
    public fun equate(o1: T?, o2: T?): Boolean

    public companion object {
        public fun <E> default(): Equator<E> = Equator { o1, o2 -> o1 == o2 }
    }
}
