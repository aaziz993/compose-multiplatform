package klib.data.type.primitives.string.fuzzywuzzy

public class DefaultStringFunction : ToStringFunction<String> {

    /**
     * Performs the default string processing on the item string
     *
     * @param `item` Input string
     * @return The processed string
     */
    override fun apply(item: String): String =
        item.replace(NON_ALPHANUMERIC_REGEX, " ").lowercase().trim { it <= ' ' }

    public companion object {

        private val NON_ALPHANUMERIC_REGEX by lazy {
            Regex("[^A-Za-z0-9 ]")
        }
    }
}
