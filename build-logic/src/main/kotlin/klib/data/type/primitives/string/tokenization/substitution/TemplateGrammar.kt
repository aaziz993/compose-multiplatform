package klib.data.type.primitives.string.tokenization.substitution

import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import klib.data.type.primitives.string.DOUBLE_QUOTED_STRING_PATTERN
import klib.data.type.primitives.string.ID_PATTERN
import klib.data.type.primitives.string.tokenization.mapWithMatches
import kotlin.collections.List
import kotlin.collections.Set
import kotlin.collections.buildList
import kotlin.collections.first
import kotlin.collections.isNotEmpty
import kotlin.collections.joinToString
import kotlin.collections.listOf
import kotlin.collections.single
import kotlin.collections.sumOf
import kotlin.collections.take
import kotlin.collections.toSet
import kotlin.sequences.toList
import kotlin.text.take

private val EVEN_DOLLARS_REGEX = Regex("""(?:\$\$)+""")
private val INTERPOLATE_REGEX = Regex("""\$($ID_PATTERN|\d+)""")
private val KEY_REGEX = Regex("""\s*($ID_PATTERN|\d+|$DOUBLE_QUOTED_STRING_PATTERN)\s*""")
private val INTERPOLATE_START_REGEX = Regex("""\$\{""")
private val EVALUATE_START_REGEX = Regex("""\$<""")
private val OTHER_REGEX = Regex("""[^$]+""")

public fun String.substitute(
    vararg options: SubstituteOption = arrayOf(
        SubstituteOption.INTERPOLATE_BRACED,
        SubstituteOption.DEEP_INTERPOLATION,
        SubstituteOption.ESCAPE_DOLLARS,
        SubstituteOption.EVALUATE,
        SubstituteOption.ESCAPE_BACKSLASHES,
    ),
    getter: (path: List<String>) -> Any? = { null },
    evaluator: (programScript: String, program: Program) -> Any? = { _, program ->
        program { name -> getter(listOf(name)) }
    }
): Any? = TemplateGrammar(options.toSet(), getter, evaluator).parseToEnd(this)

@Suppress("UNUSED")
private class TemplateGrammar(
    private val options: Set<SubstituteOption>,
    private val getter: (path: List<String>) -> Any?,
    private val evaluator: (text: String, Program) -> Any?
) {

    fun parseToEnd(input: String): Any? = buildList {
        var index = 0

        while (index < input.length) {
            if (SubstituteOption.INTERPOLATE in options || SubstituteOption.INTERPOLATE_BRACED in options || SubstituteOption.EVALUATE in options)
                EVEN_DOLLARS_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val dollars = match.groupValues.first()

                    add(if (SubstituteOption.ESCAPE_DOLLARS in options) dollars.take(dollars.length / 2) else dollars)

                    continue
                }

            if (SubstituteOption.INTERPOLATE in options)
                INTERPOLATE_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val value = getter(listOf(match.groupValues[1]))

                    add(
                        if (SubstituteOption.DEEP_INTERPOLATION in options && value is String) parseToEnd(value)
                        else value
                    )

                    continue
                }

            if (SubstituteOption.INTERPOLATE_BRACED in options)
                INTERPOLATE_START_REGEX.matchAt(input, index)?.let { match ->
                    index += match.value.length

                    val value = getter(
                        buildList {
                            while (true) {
                                KEY_REGEX.matchAt(input, index)?.let { match ->
                                    add(match.groupValues[1].removeSurrounding("\""))
                                    index += match.value.length
                                } ?: break

                                if (input[index] == '.') index++
                            }
                            check(isNotEmpty()) { "Empty interpolation" }
                            check(input.getOrNull(index) == '}') { "Missing closing brace at position $index" }
                            index++
                        },
                    )

                    add(
                        if (SubstituteOption.DEEP_INTERPOLATION in options && value is String) parseToEnd(value)
                        else value
                    )

                    continue
                }

            if (SubstituteOption.EVALUATE in options)
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
