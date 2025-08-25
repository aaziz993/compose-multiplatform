package klib.data.type.primitives.string.fuzzywuzzy

import klib.data.type.collections.topKElements
import klib.data.type.collections.topKHeap
import klib.data.type.primitives.string.fuzzywuzzy.model.BoundExtractedResult
import klib.data.type.primitives.string.fuzzywuzzy.model.ExtractedResult

public class Extractor(private val cutoff: Int = 0) {

    /**
     * Returns the list of choices with their associated scores of similarity in a list
     * of [ExtractedResult]
     *
     * @param query The query string
     * @param choices The list of choices
     * @param func The function to apply
     * @return The list of results
     */
    public fun extractWithoutOrder(
        query: String,
        choices: Iterable<String>,
        func: Applicable
    ): List<ExtractedResult> {
        val yields = ArrayList<ExtractedResult>()

        for ((index, s) in choices.withIndex()) {

            val score = func.apply(query, s)

            if (score >= cutoff) {
                yields.add(ExtractedResult(s, score, index))
            }
        }

        return yields
    }

    /**
     * Returns the list of choices with their associated scores of similarity in a list
     * of [ExtractedResult]
     *
     * @param query The query string
     * @param choices The list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func The function to apply
     * @return The list of results
     */
    public fun <T> extractWithoutOrder(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable
    ): List<BoundExtractedResult<T>> {

        val yields = ArrayList<BoundExtractedResult<T>>()

        for ((index, t) in choices.withIndex()) {

            val s = toStringFunction.apply(t)
            val score = func.apply(query, s)

            if (score >= cutoff) {
                yields.add(BoundExtractedResult(t, s, score, index))
            }
        }

        return yields
    }

    /**
     * Find the single best match above a score in a list of choices.
     *
     * @param query  A string to match against
     * @param choices A list of choices
     * @param func   Scoring function
     * @return An object containing the best match and it's score
     */
    public fun extractOne(query: String, choices: Iterable<String>, func: Applicable): ExtractedResult =
        extractWithoutOrder(query, choices, func).max()

    /**
     * Find the single best match above a score in a list of choices.
     *
     * @param query  A string to match against
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param func   Scoring function
     * @return An object containing the best match and it's score
     */
    public fun <T> extractOne(
        query: String,
        choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable
    ): BoundExtractedResult<T> = extractWithoutOrder(query, choices, toStringFunction, func).max()

    /**
     * Creates a **sorted** list of [ExtractedResult]  which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param func    The scoring function
     * @return A list of the results
     */
    public fun extractTop(query: String, choices: Iterable<String>, func: Applicable): List<ExtractedResult> =
        extractWithoutOrder(query, choices, func).sortedDescending()

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
        func: Applicable
    ): List<BoundExtractedResult<T>> = extractWithoutOrder(query, choices, toStringFunction, func).sortedDescending()

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param limit   Limits the number of results and speeds up
     * the search (k-top heap sort) is used
     * @return A list of the results
     */
    public fun extractTop(
        query: String,
        choices: Iterable<String>,
        func: Applicable,
        limit: Int
    ): List<ExtractedResult> = extractWithoutOrder(query, choices, func).topKElements(limit).sortedDescending()

    /**
     * Creates a **sorted** list of [ExtractedResult] which contain the
     * top @param limit most similar choices
     *
     * @param query   The query string
     * @param choices A list of choices
     * @param toStringFunction The ToStringFunction to be applied to all choices.
     * @param limit   Limits the number of results and speeds up
     * the search (k-top heap sort) is used
     * @return A list of the results
     */
    public fun <T> extractTop(
        query: String, choices: Iterable<T>,
        toStringFunction: ToStringFunction<T>,
        func: Applicable,
        limit: Int
    ): List<BoundExtractedResult<T>> = extractWithoutOrder(query, choices, toStringFunction, func)
        .topKElements(limit)
        .sortedDescending()
}
