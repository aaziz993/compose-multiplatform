/*
 * Copyright (C) 2009-2023 the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package klib.data.type.primitives.string.ansi

import kotlin.text.StringBuilder

/**
 * Renders ANSI color escape-codes in strings by parsing out some special syntax to pick up the correct fluff to use.
 *
 * The syntax for embedded ANSI codes is:
 *
 * <pre>
 * &#64;|*code*(,*code*)* *text*|&#64;
</pre> *
 *
 * Examples:
 *
 * <pre>
 * &#64;|bold Hello|&#64;
</pre> *
 *
 * <pre>
 * &#64;|bold,red Warning!|&#64;
</pre> *
 *
 * @since 2.2
 */
public object AnsiRenderer {

    public const val BEGIN_TOKEN: String = "@|"

    public const val END_TOKEN: String = "|@"

    public const val CODE_TEXT_SEPARATOR: String = " "

    public const val CODE_LIST_SEPARATOR: String = ","

    private const val BEGIN_TOKEN_LEN = 2

    private const val END_TOKEN_LEN = 2

    public fun render(input: String): String = render(input, StringBuilder()).toString()

    /**
     * Renders the given input to the target Appendable.
     *
     * @param input
     * source to render
     * @param target
     * render onto this target Appendable.
     * @return the given Appendable
     * If an I/O error occurs
     */
    public fun render(input: String, target: Appendable): Appendable {
        var i = 0
        var j: Int
        var k: Int

        while (true) {
            j = input.indexOf(BEGIN_TOKEN, i)
            if (j == -1) {
                if (i == 0) {
                    target.append(input)
                    return target
                }
                target.append(input.substring(i))
                return target
            }
            target.append(input.substring(i, j))
            k = input.indexOf(END_TOKEN, j)

            if (k == -1) {
                target.append(input)
                return target
            }
            j += BEGIN_TOKEN_LEN

            // Check for invalid string with END_TOKEN before BEGIN_TOKEN
            require(k >= j) { "Invalid input string found." }
            val spec = input.substring(j, k)

            val items = spec.split(CODE_TEXT_SEPARATOR.toRegex(), limit = 2).toTypedArray()
            if (items.size == 1) {
                target.append(input)
                return target
            }
            val replacement = render(
                items[1],
                *items.first().split(CODE_LIST_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
            )

            target.append(replacement)

            i = k + END_TOKEN_LEN
        }
    }

    public fun render(text: String, vararg codes: String): String =
        _render(Ansi.ansi(), *codes).attribute(text).reset().toString()

    /**
     * Renders names as an ANSI escape string.
     * @param codes The code names to render
     * @return an ANSI escape string.
     */
    public fun renderCodes(vararg codes: String): String = _render(Ansi.ansi(), *codes).toString()

    /**
     * Renders names as an ANSI escape string.
     * @param codes A space separated list of code names to render
     * @return an ANSI escape string.
     */
    public fun renderCodes(codes: String): String =
        renderCodes(*codes.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

    @Suppress("FunctionName")
    private fun _render(ansi: Ansi, vararg names: String): Ansi =
        names.map(String::uppercase).fold(ansi) { acc, name ->
            AnsiColor.parseOrNull(name)?.let(acc::attribute) ?: acc.attribute(Attribute.valueOf(name))
        }

    public fun test(text: String): Boolean = text.contains(BEGIN_TOKEN)
}



