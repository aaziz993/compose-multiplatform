package klib.data.type.primitives.string.fuzzywuzzy

@Suppress("SameParameterValue")
public class TokenSort : RatioAlgorithm() {

    override fun apply(s1: String, s2: String, ratio: Ratio, stringFunction: ToStringFunction<String>): Int =
        ratio.apply(processAndSort(s1, stringFunction), processAndSort(s2, stringFunction))

    private fun processAndSort(input: String, stringProcessor: ToStringFunction<String>): String =
        stringProcessor.apply(input).tokenize().sortedJoinToString(" ")

    public companion object {

        private fun String.tokenize() = split("\\s+".toRegex()).dropLastWhile(String::isEmpty)

        private fun Collection<String>.sortedJoinToString(separator: String) =
            sorted().joinToString(separator).trim { it <= ' ' }
    }
}
