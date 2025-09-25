package klib.data.type.primitives.string.fuzzywuzzy

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

public class WeightedRatio : BasicAlgorithm() {

    override fun apply(s1: String, s2: String, stringProcessor: ToStringFunction<String>): Int {

        val s1Copy = stringProcessor.apply(s1)
        val s2Copy = stringProcessor.apply(s2)

        val len1 = s1Copy.length
        val len2 = s2Copy.length

        if (len1 == 0 || len2 == 0) {
            return 0
        }

        var tryPartials = TRY_PARTIALS
        val unbaseScale = UNBASE_SCALE
        var partialScale = PARTIAL_SCALE

        val base = FuzzySearch.ratio(s1Copy, s2Copy)
        val lenRatio = max(len1, len2).toDouble() / min(len1, len2)

        // if strings are similar length don't use partials
        if (lenRatio < 1.5) tryPartials = false

        // if one string is much shorter than the other
        if (lenRatio > 8) partialScale = .6

        if (tryPartials) {

            val partial = FuzzySearch.partialRatio(s1Copy, s2Copy) * partialScale
            val partialSor = FuzzySearch.tokenSortPartialRatio(s1Copy, s2Copy) * unbaseScale * partialScale
            val partialSet = FuzzySearch.tokenSetPartialRatio(s1Copy, s2Copy) * unbaseScale * partialScale

            return round(max(max(max(base.toDouble(), partial), partialSor), partialSet)).toInt()
        }
        else {

            val tokenSort = FuzzySearch.tokenSortRatio(s1Copy, s2Copy) * unbaseScale
            val tokenSet = FuzzySearch.tokenSetRatio(s1Copy, s2Copy) * unbaseScale

            return round(max(max(base.toDouble(), tokenSort), tokenSet)).toInt()
        }
    }

    public companion object {

        public const val UNBASE_SCALE: Double = .95
        public const val PARTIAL_SCALE: Double = .90
        public const val TRY_PARTIALS: Boolean = true
    }
}
