package klib.data.type.primitives.string.fuzzywuzzy

import klib.data.type.primitives.string.fuzzywuzzy.model.BoundExtractedResult
import klib.data.type.primitives.string.fuzzywuzzy.model.ExtractedResult

/**
 * FuzzySearch facade class
 */
public object FuzzySearch {

    /**
     * Calculates a Levenshtein simple ratio between the strings.
     * This is indicates a measure of similarity
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The simple ratio
     */
    public fun ratio(s1: String, s2: String): Int = SimpleRatio().apply(s1, s2)

    /**
     * Calculates a Levenshtein simple ratio between the strings.
     * This is indicates a measure of similarity
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The simple ratio
     */
    public fun ratio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        SimpleRatio().apply(s1, s2, stringFunction)

    /**
     * Inconsistent substrings lead to problems in matching. This ratio
     * uses a heuristic called "best partial" for when two strings
     * are of noticeably different lengths.
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The partial ratio
     */
    public fun partialRatio(s1: String, s2: String): Int = PartialRatio().apply(s1, s2)

    /**
     * Inconsistent substrings lead to problems in matching. This ratio
     * uses a heuristic called "best partial" for when two strings
     * are of noticeably different lengths.
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The partial ratio
     */
    public fun partialRatio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        PartialRatio().apply(s1, s2, stringFunction)

    /**
     * Find all alphanumeric tokens in the string and sort
     * those tokens and then take ratio of resulting
     * joined strings.
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The partial ratio of the strings
     */
    public fun tokenSortPartialRatio(s1: String, s2: String): Int = TokenSort().apply(s1, s2, PartialRatio())

    /**
     * Find all alphanumeric tokens in the string and sort
     * those tokens and then take ratio of resulting
     * joined strings.
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The partial ratio of the strings
     */
    public fun tokenSortPartialRatio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        TokenSort().apply(s1, s2, PartialRatio(), stringFunction)

    /**
     * Find all alphanumeric tokens in the string and sort
     * those tokens and then take ratio of resulting
     * joined strings.
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The full ratio of the strings
     */
    public fun tokenSortRatio(s1: String, s2: String): Int = TokenSort().apply(s1, s2, SimpleRatio())

    /**
     * Find all alphanumeric tokens in the string and sort
     * those tokens and then take ratio of resulting
     * joined strings.
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The full ratio of the strings
     */
    public fun tokenSortRatio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        TokenSort().apply(s1, s2, SimpleRatio(), stringFunction)

    /**
     * Splits the strings into tokens and computes intersections and remainders
     * between the tokens of the two strings. A comparison string is then
     * built up and is compared using the simple ratio algorithm.
     * Useful for strings where words appear redundantly.
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The ratio of similarity
     */
    public fun tokenSetRatio(s1: String, s2: String): Int = TokenSet().apply(s1, s2, SimpleRatio())

    /**
     * Splits the strings into tokens and computes intersections and remainders
     * between the tokens of the two strings. A comparison string is then
     * built up and is compared using the simple ratio algorithm.
     * Useful for strings where words appear redundantly.
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The ratio of similarity
     */
    public fun tokenSetRatio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        TokenSet().apply(s1, s2, SimpleRatio(), stringFunction)

    /**
     * Splits the strings into tokens and computes intersections and remainders
     * between the tokens of the two strings. A comparison string is then
     * built up and is compared using the simple ratio algorithm.
     * Useful for strings where words appear redundantly.
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The ratio of similarity
     */
    public fun tokenSetPartialRatio(s1: String, s2: String): Int = TokenSet().apply(s1, s2, PartialRatio())

    /**
     * Splits the strings into tokens and computes intersections and remainders
     * between the tokens of the two strings. A comparison string is then
     * built up and is compared using the simple ratio algorithm.
     * Useful for strings where words appear redundantly.
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The ratio of similarity
     */
    public fun tokenSetPartialRatio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        TokenSet().apply(s1, s2, PartialRatio(), stringFunction)

    /**
     * Calculates a weighted ratio between the different algorithms for best results
     *
     * @param s1 Input string
     * @param s2 Input string
     * @return The ratio of similarity
     */
    public fun weightedRatio(s1: String, s2: String): Int = WeightedRatio().apply(s1, s2)

    /**
     * Calculates a weighted ratio between the different algorithms for best results
     *
     * @param s1              Input string
     * @param s2              Input string
     * @param stringFunction Functor which transforms strings before
     * calculating the ratio
     * @return The ratio of similarity
     */
    public fun weightedRatio(s1: String, s2: String, stringFunction: ToStringFunction<String>): Int =
        WeightedRatio().apply(s1, s2, stringFunction)

    /**
     * Creates a **sorted** list of [ExtractedResult]  which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun extractTop(
        query: String,
        choices: Iterable<String>,
        func: Applicable,
        limit: Int,
        cutoff: Int
    ): List<ExtractedResult> = Extractor(cutoff).extractTop(query, choices, func, limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param limit   Limits the number of results and speeds up
     * the search (k-top heap sort) is used
     * @param cutoff  Rejects any entries with score below this
     * @return A list of the results
     */
    public fun extractTop(
        query: String,
        choices: Iterable<String>,
        limit: Int,
        cutoff: Int
    ): List<ExtractedResult> = Extractor(cutoff).extractTop(query, choices, WeightedRatio(), limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @param limit   The number of results to return
     * @return A list of the results
     */
    public fun extractTop(
        query: String,
        choices: Iterable<String>,
        func: Applicable,
        limit: Int
    ): List<ExtractedResult> = Extractor().extractTop(query, choices, func, limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param limit   The number of results to return
     * @return A list of the results
     */
    public fun extractTop(
        query: String,
        choices: Iterable<String>,
        limit: Int
    ): List<ExtractedResult> = Extractor().extractTop(query, choices, WeightedRatio(), limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun extractSorted(
        query: String,
        choices: Iterable<String>,
        func: Applicable
    ): List<ExtractedResult> = Extractor().extractTop(query, choices, func)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun extractSorted(
        query: String,
        choices: Iterable<String>,
        func: Applicable,
        cutoff: Int
    ): List<ExtractedResult> = Extractor(cutoff).extractTop(query, choices, func)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @return A list of the results
     */
    public fun extractSorted(query: String, choices: Iterable<String>): List<ExtractedResult> =
        Extractor().extractTop(query, choices, WeightedRatio())

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun extractSorted(query: String, choices: Iterable<String>, cutoff: Int): List<ExtractedResult> =
        Extractor(cutoff).extractTop(query, choices, WeightedRatio())

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun extractAll(query: String, choices: Iterable<String>, func: Applicable): List<ExtractedResult> =
        Extractor().extractWithoutOrder(query, choices, func)

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun extractAll(
        query: String,
        choices: Iterable<String>,
        func: Applicable,
        cutoff: Int
    ): List<ExtractedResult> = Extractor(cutoff).extractWithoutOrder(query, choices, func)

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @return A list of the results
     */
    public fun extractAll(query: String, choices: Iterable<String>): List<ExtractedResult> =
        Extractor().extractWithoutOrder(query, choices, WeightedRatio())

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun extractAll(query: String, choices: Iterable<String>, cutoff: Int): List<ExtractedResult> =
        Extractor(cutoff).extractWithoutOrder(query, choices, WeightedRatio())

    /**
     * Find the single best match above a score in a list of choices.
     *
     * @param query   A string to match against
     * @param choices A list of choices
     * @param func    Scoring function
     * @return An object containing the best match and it's score
     */
    public fun extractOne(query: String, choices: Iterable<String>, func: Applicable): ExtractedResult =
        Extractor().extractOne(query, choices, func)

    /**
     * Find the single best match above a score in a list of choices.
     *
     * @param query   A string to match against
     * @param choices A list of choices
     * @return An object containing the best match and it's score
     */
    public fun extractOne(query: String, choices: Iterable<String>): ExtractedResult =
        Extractor().extractOne(query, choices, WeightedRatio())

    /**
     * Creates a **sorted** list of [ExtractedResult]  which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun <T> extractTop(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable,
        limit: Int,
        cutoff: Int
    ): List<BoundExtractedResult<T>> = Extractor(cutoff).extractTop(query, choices, toStringFunction, func, limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param limit   Limits the number of results and speeds up
     * the search (k-top heap sort) is used
     * @param cutoff  Rejects any entries with score below this
     * @return A list of the results
     */
    public fun <T> extractTop(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        limit: Int,
        cutoff: Int
    ): List<BoundExtractedResult<T>> = Extractor(cutoff).extractTop(query, choices, toStringFunction,
        WeightedRatio(), limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    The scoring function
     * @param limit   The number of results to return
     * @return A list of the results
     */
    public fun <T> extractTop(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable,
        limit: Int
    ): List<BoundExtractedResult<T>> = Extractor().extractTop(query, choices, toStringFunction, func, limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param limit   The number of results to return
     * @return A list of the results
     */
    public fun <T> extractTop(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        limit: Int
    ): List<BoundExtractedResult<T>> = Extractor().extractTop(query, choices, toStringFunction, WeightedRatio(), limit)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun <T> extractSorted(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable
    ): List<BoundExtractedResult<T>> = Extractor().extractTop(query, choices, toStringFunction, func)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    The scoring function
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun <T> extractSorted(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable,
        cutoff: Int
    ): List<BoundExtractedResult<T>> = Extractor(cutoff).extractTop(query, choices, toStringFunction, func)

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @return A list of the results
     */
    public fun <T> extractSorted(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>
    ): List<BoundExtractedResult<T>> = Extractor().extractTop(query, choices, toStringFunction, WeightedRatio())

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain all the choices
     * with their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun <T> extractSorted(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        cutoff: Int
    ): List<BoundExtractedResult<T>> = Extractor(cutoff).extractTop(query, choices, toStringFunction, WeightedRatio())

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun <T> extractAll(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable
    ): List<BoundExtractedResult<T>> = Extractor().extractWithoutOrder(query, choices, toStringFunction, func)

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    The scoring function
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun <T> extractAll(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable,
        cutoff: Int
    ): List<BoundExtractedResult<T>> = Extractor(cutoff).extractWithoutOrder(query, choices, toStringFunction, func)

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @return A list of the results
     */
    public fun <T> extractAll(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>
    ): List<BoundExtractedResult<T>> = Extractor().extractWithoutOrder(query, choices, toStringFunction,
        WeightedRatio()
    )

    /**
     * Creates a list of [ExtractedResult] which contain all the choices with
     * their corresponding score where higher is more similar
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param cutoff  Keep only scores above cutoff
     * @return A list of the results
     */
    public fun <T> extractAll(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        cutoff: Int
    ): List<BoundExtractedResult<T>> = Extractor(cutoff).extractWithoutOrder(query, choices, toStringFunction,
        WeightedRatio()
    )

    /**
     * Find the single best match above a score in a list of choices.
     *
     * @param query   A string to match against
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func    Scoring function
     * @return An object containing the best match and it's score
     */
    public fun <T> extractOne(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable
    ): BoundExtractedResult<T> = Extractor().extractOne(query, choices, toStringFunction, func)

    /**
     * Find the single best match above a score in a list of choices.
     *
     * @param query   A string to match against
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @return An object containing the best match and it's score
     */
    public fun <T> extractOne(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>
    ): BoundExtractedResult<T> = Extractor().extractOne(query, choices, toStringFunction, WeightedRatio())
}
