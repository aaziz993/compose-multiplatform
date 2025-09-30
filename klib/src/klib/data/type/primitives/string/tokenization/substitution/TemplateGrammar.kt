package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.primitives.string.DOUBLE_QUOTED_STRING_PLAIN_PATTERN
import klib.data.type.primitives.string.tokenization.lexer.TokenMatch
import klib.data.type.primitives.string.tokenization.parser.toParsedOrThrow
import klib.data.type.primitives.string.ID
import klib.data.type.primitives.string.tokenization.combinators.mapWithMatches

private val EVEN_DOLLARS_REGEX = Regex("""(?:\$\$)+""")
private val NULL_KEY_REGEX = Regex("""\s*(null)\s*""")
private val INTEGER_KEY_REGEX = Regex("""\s*(\d+)\s*""")
private val KEY_REGEX = Regex("""\s*((?:${Regex.ID}|[\d-])+)\s*""")
private val DOUBLE_QUOTED_STRING_KEY_REGEX = Regex("""\s*"(${Regex.DOUBLE_QUOTED_STRING_PLAIN_PATTERN})"\s*""")
private val INTERPOLATE_START_REGEX = Regex("""\$""")
private val INTERPOLATE_BRACED_START_REGEX = Regex("""\$\{""")
private val EVALUATE_START_REGEX = Regex("""\$<""")
private val OTHER_REGEX = Regex("""[^$]+""")

public fun String.substitute(
    interpolate: Boolean = false,
    interpolateBraced: Boolean = true,
    evaluate: Boolean = true,
    unescapeDollars: Boolean = true,
    getter: (path: List<Any?>) -> Any? = { null },
    evaluator: (programScript: String, program: Program) -> Any? = { _, program -> program(getter) },
    cache: MutableMap<String, Any?> = mutableMapOf(),
): Any? = TemplateGrammar(
    interpolate,
    interpolateBraced,
    evaluate,
    unescapeDollars,
    getter,
    evaluator,
    cache,
).parseToEnd(this)

private class TemplateGrammar(
    private val interpolate: Boolean,
    private val interpolateBraced: Boolean,
    private val evaluate: Boolean,
    private val unescapeDollars: Boolean,
    private val getter: (path: List<Any?>) -> Any?,
    private val evaluator: (text: String, Program) -> Any?,
    private val cache: MutableMap<String, Any?>
) {
    fun parseToEnd(input: String): Any? {
        var index = 0
        var notFound = false

        return buildList {
            while (index < input.length) {
                if (interpolate || interpolateBraced || evaluate)
                    EVEN_DOLLARS_REGEX.matchAt(input, index)?.let { match ->
                        index += match.value.length

                        val dollars = match.groupValues.first()

                        add(if (unescapeDollars) dollars.take(dollars.length / 2) else dollars)
                        continue
                    }

                if (interpolateBraced)
                    INTERPOLATE_BRACED_START_REGEX.matchAt(input, index)?.let { match ->
                        val offset = index

                        index += match.value.length

                        val path = buildList {
                            while (true) {
                                index += matchKey(input, index).takeIf { it > 0 } ?: break

                                if (index >= input.length) error("Missing } at '${input.length}' in '$input'")

                                if (input[index] == '.') index++
                            }
                            check(isNotEmpty()) { "Empty interpolate at '$offset' in '$input'" }
                            check(input.getOrNull(index) == '}') { "Missing } at '$index' in '$input'" }
                            index++
                        }
                        try {
                            add(getValue(path))
                        } catch (_: NoSuchElementException) {
                            add(input.subSequence(offset, index))
                            notFound = true
                        }

                        continue
                    }

                if (interpolate)
                    INTERPOLATE_START_REGEX.matchAt(input, index)?.let { match ->
                        val offset = index

                        index += match.value.length

                        val path = buildList {
                            while (true) {
                                index += matchKey(input, index).takeIf { it > 0 } ?: break

                                if (index >= input.length) break

                                if (input[index] == '.') index++
                            }
                        }

                        if (path.isEmpty()) index = offset
                        else {
                            try {
                                add(getValue(path))
                            } catch (_: NoSuchElementException) {
                                add(input.subSequence(offset, index))
                                notFound = true
                            }

                            continue
                        }
                    }

                if (evaluate)
                    EVALUATE_START_REGEX.matchAt(input, index)?.let { match ->
                        val offset = index

                        index += match.value.length

                        val tokens = ProgramGrammar.tokenizer.tokenize(input.substring(index))

                        val (programScript, program) = ProgramGrammar.mapWithMatches { (matches, program) ->
                            matches.joinToString("", transform = TokenMatch::text) to program
                        }.tryParse(tokens, 0).toParsedOrThrow().value

                        index += programScript.length

                        check(input.getOrNull(index) == '>') { "Missing > at '$index' in '$input'" }

                        index++

                        try {
                            add(evaluator(programScript, program))
                        } catch (_: NoSuchElementException) {
                            add(input.subSequence(offset, index))
                            notFound = true
                        }

                        continue
                    }

                OTHER_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    add(match.value)
                    continue
                }

                add(input[index++])
            }
        }.let { values ->
            val value = if (values.size == 1) values.single() else values.joinToString("")
            if (notFound) throw NoSuchElementException(value.toString()) else value
        }
    }

    private fun MutableList<Any?>.matchKey(input: String, index: Int): Int =
        NULL_KEY_REGEX.matchAt(input, index)?.let { match ->
            add(null)
            match.value.length
        } ?: INTEGER_KEY_REGEX.matchAt(input, index)?.let { match ->
            add(match.groupValues[1].toInt())
            match.value.length
        } ?: KEY_REGEX.matchAt(input, index)?.let { match ->
            add(match.groupValues[1])
            match.value.length
        } ?: DOUBLE_QUOTED_STRING_KEY_REGEX.matchAt(input, index)?.let { match ->
            add(match.groupValues[1].toInt())
            match.value.length
        } ?: 0

    private fun getValue(path: List<Any?>): Any? {
        val pathPlain = path.joinToString(".")

        return if (pathPlain in cache) cache[pathPlain]
        else getter(path).let { value ->
            if (value is String) parseToEnd(value) else value
        }.also { value -> cache[pathPlain] = value }
    }
}
