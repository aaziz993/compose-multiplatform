package klib.data.type.primitives.string.tokenization.evaluation.program

import klib.data.type.primitives.string.tokenization.evaluation.invoke

public data class Program(
    val mainFunction: Function,
    val functions: List<Function>,
) {
    public operator fun invoke(input: (name: String) -> Any?): Any? =
        NaiveProgramInterpreter()(this, input).run {
            log.forEach(::println)

            exceptionType?.let { type -> throw type(result) as Throwable }

            result
        }
}