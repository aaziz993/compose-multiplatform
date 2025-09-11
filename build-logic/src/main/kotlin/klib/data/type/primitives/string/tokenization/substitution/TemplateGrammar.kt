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
import kotlin.collections.joinToString
import kotlin.collections.listOf
import kotlin.collections.single
import kotlin.collections.sumOf
import kotlin.collections.take
import kotlin.collections.toSet
import kotlin.sequences.map
import kotlin.sequences.toList

private val INTERPOLATE_REGEX = Regex($$"""(\$+)($$ID_PATTERN)""")
private const val KEY_PATTERN = """(?:$ID_PATTERN|\d+|$DOUBLE_QUOTED_STRING_PATTERN)"""
private val KEY_REGEX = Regex(KEY_PATTERN)
private val INTERPOLATE_BRACED_REGEX = Regex($$"""(\$+)\{(\s*$$KEY_PATTERN(?:\s*\.\s*$$KEY_PATTERN)*\s*)\}""")
private val EVEN_DOLLARS_REGEX = Regex("""(?:\$\$)+""")
private val EVALUATE_START_REGEX = Regex("""(\\*)\{""")
private val OTHER_REGEX = Regex("""[^$\\{]+""")

public fun String.substitute(
    vararg options: SubstituteOption = arrayOf(
        SubstituteOption.INTERPOLATE_BRACES,
        SubstituteOption.DEEP_INTERPOLATION,
        SubstituteOption.ESCAPE_DOLLARS,
        SubstituteOption.EVALUATE,
        SubstituteOption.ESCAPE_BACKSLASHES,
    ),
    getter: (path: List<String>) -> Any? = { null },
    evaluator: (text: String, Program) -> Any? = { _, program -> program { name -> getter(listOf(name)) } }
): Any? = TemplateGrammar(options.toSet(), getter, evaluator).parseToEnd(this)

@Suppress("UNUSED")
private class TemplateGrammar(
    private val options: Set<SubstituteOption>,
    getter: (path: List<String>) -> Any?,
    private val evaluator: (text: String, Program) -> Any?
) {

    private val dollarsEscaper: (String) -> String =
        if (SubstituteOption.ESCAPE_DOLLARS in options) { dollars -> dollars.take(dollars.length / 2) }
        else { dollars -> dollars }

    private val backslashEscaper: (String) -> String =
        if (SubstituteOption.ESCAPE_BACKSLASHES in options) { backslashes ->
            backslashes.take(backslashes.length / 2)
        }
        else { backslashes -> backslashes }

    private val valueGetter: (path: List<String>) -> Any? =
        if (SubstituteOption.DEEP_INTERPOLATION in options) { path ->
            getter(path)?.let { value -> if (value is String) parseToEnd(value) else value }
        }
        else getter

    fun parseToEnd(value: String): Any? = buildList {
        var i = 0

        while (i < value.length) {
            if (SubstituteOption.INTERPOLATE in options)
                INTERPOLATE_REGEX.matchAt(value, i)?.let { match ->
                    var dollars = match.groupValues[1]
                    val key = match.groupValues[2]
                    var value: Any? = key

                    if (dollars.length % 2 != 0) {
                        dollars = dollars.drop(1)
                        value = valueGetter(listOf(key))
                    }

                    if (dollars.isNotEmpty()) add(dollarsEscaper(dollars))
                    add(value)

                    i += match.value.length
                    continue
                }

            if (SubstituteOption.INTERPOLATE_BRACES in options)
                INTERPOLATE_BRACED_REGEX.matchAt(value, i)?.let { match ->
                    var dollars = match.groupValues[1]
                    val path = match.groupValues[2]
                    var value: Any? = "{$path}"

                    if (dollars.length % 2 != 0) {
                        dollars = dollars.drop(1)
                        value = valueGetter(
                            KEY_REGEX
                                .findAll(path)
                                .map { it.groupValues.first().removeSurrounding("\"") }
                                .toList(),
                        )
                    }

                    if (dollars.isNotEmpty()) add(dollarsEscaper(dollars))
                    add(value)

                    i += match.value.length
                    continue
                }

            if (SubstituteOption.INTERPOLATE in options || SubstituteOption.INTERPOLATE_BRACES in options)
                EVEN_DOLLARS_REGEX.matchAt(value, i)?.let { match ->
                    val dollars = match.groupValues.first()

                    add(dollarsEscaper(dollars))

                    i += match.value.length
                    continue
                }

            if (SubstituteOption.EVALUATE in options)
                EVALUATE_START_REGEX.matchAt(value, i)?.let { match ->
                    var backslashes = match.groupValues[1]
                    var result: Any? = "{"

                    i += backslashes.length + 1

                    if (backslashes.length % 2 == 0) {
                        val tokens = ProgramGrammar.tokenizer.tokenize(value.substring(i))
                        val tokenList = tokens.toList()

                        val parseResult = ProgramGrammar.mapWithMatches { (matches, program) ->
                            evaluator(matches.joinToString("", transform = TokenMatch::text), program)
                        }.tryParse(tokens, 0).toParsedOrThrow()

                        result = parseResult.value

                        if (parseResult.nextPosition >= tokenList.size || tokenList[parseResult.nextPosition].text != "}")
                            error("Closing brackets not found")

                        i += tokenList.take(parseResult.nextPosition + 1).sumOf { token -> token.text.length }
                    } else backslashes = backslashes.drop(1)

                    if (backslashes.isNotEmpty()) add(backslashEscaper(backslashes))
                    add(result)

                    continue
                }

            OTHER_REGEX.matchAt(value, i)?.let { match ->
                add(match.value)
                i += match.value.length
                continue
            }

            add(value[i++])
        }
    }.let { values ->
        if (values.size == 1) values.single() else values.joinToString("")
    }
}
