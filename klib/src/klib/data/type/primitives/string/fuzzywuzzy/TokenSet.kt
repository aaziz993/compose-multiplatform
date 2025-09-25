package klib.data.type.primitives.string.fuzzywuzzy

import klib.data.type.collections.symmetricMinus

@Suppress("SameParameterValue")
public class TokenSet : RatioAlgorithm() {

    override fun apply(s1: String, s2: String, ratio: Ratio, stringFunction: ToStringFunction<String>): Int {
        val tokens1 = stringFunction.apply(s1).tokenize()
        val tokens2 = stringFunction.apply(s2).tokenize()

        val intersection = tokens1.intersect(tokens2)
        val (diff1to2, diff2to1) = tokens1 symmetricMinus tokens2

        val sortedInter = intersection.sortedJoinToString(" ").trim()
        val sorted1to2 = (sortedInter + " " + diff1to2.sortedJoinToString(" ")).trim { it <= ' ' }
        val sorted2to1 = (sortedInter + " " + diff2to1.sortedJoinToString(" ")).trim { it <= ' ' }

        val results = ArrayList<Int>()

        results.add(ratio.apply(sortedInter, sorted1to2))
        results.add(ratio.apply(sortedInter, sorted2to1))
        results.add(ratio.apply(sorted1to2, sorted2to1))

        return results.max()
    }

    public companion object {

        private fun String.tokenize() = split("\\s+".toRegex()).dropLastWhile(String::isEmpty)

        private fun Collection<String>.sortedJoinToString(separator: String) =
            sorted().joinToString(separator).trim { it <= ' ' }
    }
}
