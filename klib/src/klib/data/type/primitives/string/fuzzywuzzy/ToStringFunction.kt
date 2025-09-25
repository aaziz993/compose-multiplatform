package klib.data.type.primitives.string.fuzzywuzzy


/**
 * Transforms an item of type T to a String.
 *
 * @param <T> The type of the item to transform.
</T> */
public interface ToStringFunction<T> {
    /**
     * Transforms the input item to a string.
     *
     * @param item The item to transform.
     * @return A string to use for comparing the item.
     */
    public fun apply(item: T): String

    public companion object {

        /**
         * A default ToStringFunction that returns the input string;
         * used by methods that use plain strings in [FuzzySearch].
         */
        public val NO_PROCESS: ToStringFunction<String> = object : ToStringFunction<String> {
            override fun apply(item: String): String {
                return item
            }
        }
    }
}
