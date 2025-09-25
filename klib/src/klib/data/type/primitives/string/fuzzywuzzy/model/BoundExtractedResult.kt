package klib.data.type.primitives.string.fuzzywuzzy.model

public data class BoundExtractedResult<T>(
    val referent: T,
    var string: String?,
    val score: Int,
    val index: Int
) : Comparable<BoundExtractedResult<T>> {

    override fun toString(): String {
        return "(string: $string, score: $score, index: $index)"
    }

    override fun compareTo(other: BoundExtractedResult<T>): Int {
        return this.score.compareTo(other.score)
    }
}
