package klib.data.type.primitives.string.fuzzywuzzy

public abstract class BasicAlgorithm(
    public val stringFunction: ToStringFunction<String>? = DefaultStringFunction()
) : Applicable {

    public abstract fun apply(s1: String, s2: String, stringProcessor: ToStringFunction<String> = stringFunction!!): Int

    override fun apply(s1: String, s2: String): Int = apply(s1, s2, stringFunction!!)
}
