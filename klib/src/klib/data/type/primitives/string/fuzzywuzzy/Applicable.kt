package klib.data.type.primitives.string.fuzzywuzzy

/**
 * A ratio/algorithm that can be applied
 */

public interface Applicable {

    /**
     * Apply the ratio/algorithm to the input strings
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The score of similarity
     */
    public fun apply(s1: String, s2: String): Int

}
