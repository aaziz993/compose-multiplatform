package klib.data.type.primitives.string.fuzzywuzzy

public abstract class RatioAlgorithm(
    stringFunction: ToStringFunction<String> = DefaultStringFunction(),
    public val ratio: Ratio = SimpleRatio()
) : BasicAlgorithm(stringFunction) {

    public abstract fun apply(s1: String, s2: String, ratio: Ratio, stringFunction: ToStringFunction<String>): Int

    public fun apply(s1: String, s2: String, ratio: Ratio): Int =
        apply(s1, s2, ratio, stringFunction!!)

    override fun apply(s1: String, s2: String, stringProcessor: ToStringFunction<String>): Int = apply(s1, s2, ratio, stringProcessor)
}
