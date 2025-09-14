package klib.data.type.primitives.string.tokenization.substitution

import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import klib.data.type.primitives.string.DOUBLE_QUOTED_STRING_PATTERN
import klib.data.type.primitives.string.KEY_PATTERN
import klib.data.type.primitives.string.tokenization.mapWithMatches
import kotlin.collections.List
import kotlin.collections.buildList
import kotlin.collections.first
import kotlin.collections.isNotEmpty
import kotlin.collections.joinToString
import kotlin.collections.listOf
import kotlin.collections.single
import kotlin.collections.sumOf
import kotlin.collections.take
import kotlin.sequences.toList
import kotlin.text.take

private val EVEN_DOLLARS_REGEX = Regex("""(?:\$\$)+""")
private val INTERPOLATE_REGEX = Regex("""\$($KEY_PATTERN)""")
private val KEY_OR_STRING_REGEX = Regex("""\s*($KEY_PATTERN|$DOUBLE_QUOTED_STRING_PATTERN)\s*""")
private val INTERPOLATE_START_REGEX = Regex("""\$\{""")
private val EVALUATE_START_REGEX = Regex("""\$<""")
private val OTHER_REGEX = Regex("""[^$]+""")

public fun String.substitute(
    interpolate: Boolean = false,
    interpolateBraced: Boolean = true,
    evaluate: Boolean = true,
    unescapeDollars: Boolean = false,
    getter: (path: List<String>) -> Any? = { null },
    evaluator: (programScript: String, program: Program) -> Any? = { _, program ->
        program { name -> getter(listOf(name)) }
    }
): Any? = TemplateGrammar(
    interpolate,
    interpolateBraced,
    evaluate,
    unescapeDollars,
    getter,
    evaluator,
).parseToEnd(this)

@Suppress("UNUSED")
internal class TemplateGrammar(
    private val interpolate: Boolean = false,
    private val interpolateBraced: Boolean = true,
    private val evaluate: Boolean = true,
    private val unescapeDollars: Boolean = true,
    private val getter: (path: List<String>) -> Any?,
    private val evaluator: (text: String, Program) -> Any?
) {

    private val cache = mutableMapOf<String, Any?>()

    fun parseToEnd(input: String): Any? = buildList {
        var index = 0

        while (index < input.length) {
            if (interpolate || interpolateBraced || evaluate)
                EVEN_DOLLARS_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val dollars = match.groupValues.first()

                    add(if (unescapeDollars) dollars.take(dollars.length / 2) else dollars)
                    continue
                }

            if (interpolate)
                INTERPOLATE_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val key = match.groupValues[1]
                    var value: Any?

                    if (key in cache) value = cache[key]
                    else {
                        value = getter(listOf(key))
                        if (value is String) value = parseToEnd(value)
                        cache[key] = value
                    }

                    add(value)
                    continue
                }

            if (interpolateBraced)
                INTERPOLATE_START_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val path = buildList {
                        while (true) {
                            KEY_OR_STRING_REGEX.matchAt(input, index)?.let { match ->
                                add(match.groupValues[1].removeSurrounding("\""))
                                index += match.value.length
                            } ?: break

                            if (input[index] == '.') index++
                        }
                        check(isNotEmpty()) { "Empty interpolation" }
                        check(input.getOrNull(index) == '}') { "Missing closing brace at position $index" }
                        index++
                    }

                    val pathPlain = path.joinToString(".")
                    var value: Any?

                    if (pathPlain in cache) value = cache[pathPlain]
                    else {
                        value = getter(path)
                        if (value is String) value = parseToEnd(value)
                        cache[pathPlain] = value
                    }

                    add(value)
                    continue
                }

            if (evaluate)
                EVALUATE_START_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val tokens = ProgramGrammar.tokenizer.tokenize(input.substring(index))
                    val tokenList = tokens.toList()

                    val parseResult = ProgramGrammar.mapWithMatches { (matches, program) ->
                        evaluator(matches.joinToString("", transform = TokenMatch::text), program)
                    }.tryParse(tokens, 0).toParsedOrThrow()

                    add(parseResult.value)

                    if (parseResult.nextPosition >= tokenList.size || tokenList[parseResult.nextPosition].text != ">")
                        error("Missing closing brace")

                    index += tokenList.take(parseResult.nextPosition + 1).sumOf { token -> token.text.length }

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
        if (values.size == 1) values.single() else values.joinToString("")
    }
}
