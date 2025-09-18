package klib.data.type.primitives.string.tokenization.substitution

public data class Program(
    val mainFunction: Function,
    val functions: List<Function>,
) {
    public operator fun invoke(input: (path: List<Any?>) -> Any?): Any? =
        NaiveProgramInterpreter()(this, input).run {
            log.forEach(::println)

            exceptionType?.let { type -> throw type(result) as Throwable }

            result
        }
}
