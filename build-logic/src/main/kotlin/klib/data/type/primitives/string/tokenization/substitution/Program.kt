package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.primitives.string.tokenization.invoke

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
