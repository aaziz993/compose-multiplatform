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
package klib.data.type.ansi

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.Ansi16
import com.github.ajalt.colormath.model.Ansi256
import klib.data.type.primitives.string.format
import kotlin.math.max

public fun supportsAnsi(): Boolean = true

/**
 * Provides a fluent API for generating
 * [ANSI escape sequences](https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_sequences).
 *
 * @since 1.0
 */
public open class Ansi(private val builder: StringBuilder = StringBuilder(80)) : Appendable {

    public constructor(parent: Ansi) : this(StringBuilder(parent.builder)) {
        attributes.addAll(parent.attributes)
    }

    public constructor(size: Int) : this(StringBuilder(size))

    private val attributes = ArrayList<Int>(5)

    public open fun attribute(attribute: Ansi16): Ansi {
        attributes.add(attribute.code)
        return this
    }

    public open fun attribute(attribute: Ansi256, background: Boolean = false): Ansi {
        attributes.add(if (background) 48 else 38)
        attributes.add(5)
        attributes.add(attribute.code and 0xff)
        return this
    }

    public open fun attribute(attribute: Color, background: Boolean = false): Ansi =
        attribute(attribute.toAnsi256(), background)

    public open fun attribute(attribute: Attribute): Ansi {
        attributes.add(attribute.code)
        return this
    }

    /**
     * Moves the cursor to row n, column m. The values are 1-based.
     * Any values less than 1 are mapped to 1.
     *
     * @param row    row (1-based) from top
     * @param column column (1 based) from left
     * @return this Ansi instance
     */
    public open fun cursor(row: Int, column: Int): Ansi =
        appendEscapeSequence('H', max(1, row), max(1, column))

    /**
     * Moves the cursor to column n. The parameter n is 1-based.
     * If n is less than 1 it is moved to the first column.
     *
     * @param x the index (1-based) of the column to move to
     * @return this Ansi instance
     */
    public open fun cursorToColumn(x: Int): Ansi = appendEscapeSequence('G', max(1, x))

    /**
     * Moves the cursor up. If the parameter y is negative it moves the cursor down.
     *
     * @param y the number of lines to move up
     * @return this Ansi instance
     */
    public open fun cursorUp(y: Int): Ansi =
        if (y > 0) appendEscapeSequence('A', y) else if (y < 0) cursorDown(-y) else this

    /**
     * Moves the cursor down. If the parameter y is negative it moves the cursor up.
     *
     * @param y the number of lines to move down
     * @return this Ansi instance
     */
    public open fun cursorDown(y: Int): Ansi =
        if (y > 0) appendEscapeSequence('B', y) else if (y < 0) cursorUp(-y) else this

    /**
     * Moves the cursor right. If the parameter x is negative it moves the cursor left.
     *
     * @param x the number of characters to move right
     * @return this Ansi instance
     */
    public open fun cursorRight(x: Int): Ansi =
        if (x > 0) appendEscapeSequence('C', x) else if (x < 0) cursorLeft(-x) else this

    /**
     * Moves the cursor left. If the parameter x is negative it moves the cursor right.
     *
     * @param x the number of characters to move left
     * @return this Ansi instance
     */
    public open fun cursorLeft(x: Int): Ansi =
        if (x > 0) appendEscapeSequence('D', x) else if (x < 0) cursorRight(-x) else this

    /**
     * Moves the cursor relative to the current position. The cursor is moved right if x is
     * positive, left if negative and down if y is positive and up if negative.
     *
     * @param x the number of characters to move horizontally
     * @param y the number of lines to move vertically
     * @return this Ansi instance
     * @since 2.2
     */
    public fun cursorMove(x: Int, y: Int): Ansi = cursorRight(x).cursorDown(y)

    /**
     * Moves the cursor to the beginning of the line below.
     *
     * @return this Ansi instance
     */
    public open fun cursorDownLine(): Ansi = appendEscapeSequence('E')

    /**
     * Moves the cursor to the beginning of the n-th line below. If the parameter n is negative it
     * moves the cursor to the beginning of the n-th line above.
     *
     * @param n the number of lines to move the cursor
     * @return this Ansi instance
     */
    public open fun cursorDownLine(n: Int): Ansi =
        if (n < 0) cursorUpLine(-n) else appendEscapeSequence('E', n)

    /**
     * Moves the cursor to the beginning of the line above.
     *
     * @return this Ansi instance
     */
    public open fun cursorUpLine(): Ansi = appendEscapeSequence('F')

    /**
     * Moves the cursor to the beginning of the n-th line above. If the parameter n is negative it
     * moves the cursor to the beginning of the n-th line below.
     *
     * @param n the number of lines to move the cursor
     * @return this Ansi instance
     */
    public open fun cursorUpLine(n: Int): Ansi =
        if (n < 0) cursorDownLine(-n) else appendEscapeSequence('F', n)

    public open fun eraseScreen(): Ansi = appendEscapeSequence('J', Erase.ALL.value)

    public open fun eraseScreen(kind: Erase): Ansi = appendEscapeSequence('J', kind.value)

    public open fun eraseLine(): Ansi = appendEscapeSequence('K')

    public open fun eraseLine(kind: Erase): Ansi = appendEscapeSequence('K', kind.value)

    public open fun scrollUp(rows: Int): Ansi {
        if (rows == Int.MIN_VALUE) return scrollDown(Int.MAX_VALUE)

        return if (rows > 0) appendEscapeSequence('S', rows) else if (rows < 0) scrollDown(-rows) else this
    }

    public open fun scrollDown(rows: Int): Ansi {
        if (rows == Int.MIN_VALUE) return scrollUp(Int.MAX_VALUE)

        return if (rows > 0) appendEscapeSequence('T', rows) else if (rows < 0) scrollUp(-rows) else this
    }

    public open fun saveCursorPosition(): Ansi {
        saveCursorPositionSCO()
        return saveCursorPositionDEC()
    }

    // SCO command
    public fun saveCursorPositionSCO(): Ansi = appendEscapeSequence('s')

    // DEC command
    public fun saveCursorPositionDEC(): Ansi {
        builder.append(FIRST_ESC_CHAR)
        builder.append('7')
        return this
    }

    public open fun restoreCursorPosition(): Ansi {
        restoreCursorPositionSCO()
        return restoreCursorPositionDEC()
    }

    // SCO command
    public fun restoreCursorPositionSCO(): Ansi = appendEscapeSequence('u')

    // DEC command
    public fun restoreCursorPositionDEC(): Ansi {
        builder.append(FIRST_ESC_CHAR)
        builder.append('8')
        return this
    }

    public open fun reset(): Ansi = attribute(Attribute.RESET)

    public fun bold(): Ansi = attribute(Attribute.INTENSITY_BOLD)

    public fun boldOff(): Ansi = attribute(Attribute.INTENSITY_BOLD_OFF)

    public fun attribute(value: String): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Boolean): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Char): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: CharArray, offset: Int, len: Int): Ansi {
        flushAttributes()
        builder.append(value, offset, len)
        return this
    }

    public fun attribute(value: CharArray): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: CharSequence, start: Int, end: Int): Ansi {
        flushAttributes()
        builder.append(value, start, end)
        return this
    }

    public fun attribute(value: CharSequence): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Double): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Float): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Int): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Long): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: Any?): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun attribute(value: StringBuilder): Ansi {
        flushAttributes()
        builder.append(value)
        return this
    }

    public fun newline(): Ansi {
        flushAttributes()
        builder.appendLine()
        return this
    }

    public fun format(pattern: String, vararg args: Any?): Ansi {
        flushAttributes()
        builder.append(pattern.format(*args))
        return this
    }

    /**
     * Uses the [AnsiRenderer]
     * to generate the ANSI escape sequences for the supplied text.
     *
     * @param text text
     * @return this
     * @since 2.2
     */
    public fun render(text: String): Ansi {
        attribute(AnsiRenderer.render(text))
        return this
    }

    /**
     * String formats and renders the supplied arguments.  Uses the [AnsiRenderer]
     * to generate the ANSI escape sequences.
     *
     * @param text format
     * @param args arguments
     * @return this
     * @since 2.2
     */
    public fun render(text: String, vararg args: Any?): Ansi {
        attribute(AnsiRenderer.render(text).format(*args))
        return this
    }

    override fun toString(): String {
        flushAttributes()
        return builder.toString()
    }

    /**//////////////////////////////////////////////////////////////// */ // Private Helper Methods
    /**//////////////////////////////////////////////////////////////// */
    private fun appendEscapeSequence(command: Char): Ansi {
        flushAttributes()
        builder.append(FIRST_ESC_CHAR)
        builder.append(SECOND_ESC_CHAR)
        builder.append(command)
        return this
    }

    private fun appendEscapeSequence(command: Char, option: Int): Ansi {
        flushAttributes()
        builder.append(FIRST_ESC_CHAR)
        builder.append(SECOND_ESC_CHAR)
        builder.append(option)
        builder.append(command)
        return this
    }

    private fun appendEscapeSequence(command: Char, vararg options: Any?): Ansi {
        flushAttributes()
        return _appendEscapeSequence(command, *options)
    }

    private fun flushAttributes() {
        if (attributes.isEmpty()) return
        if (attributes.size == 1 && attributes[0] == 0) {
            builder.append(FIRST_ESC_CHAR)
            builder.append(SECOND_ESC_CHAR)
            builder.append('0')
            builder.append('m')
        }
        else _appendEscapeSequence('m', *attributes.toTypedArray())
        attributes.clear()
    }

    @Suppress("FunctionName")
    private fun _appendEscapeSequence(command: Char, vararg options: Any?): Ansi {
        builder.append(FIRST_ESC_CHAR)
        builder.append(SECOND_ESC_CHAR)
        val size = options.size
        for (i in 0..<size) {
            if (i != 0) {
                builder.append(';')
            }
            if (options[i] != null) {
                builder.append(options[i])
            }
        }
        builder.append(command)
        return this
    }

    override fun append(csq: CharSequence?): Ansi {
        builder.append(csq)
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): Ansi {
        builder.append(csq, start, end)
        return this
    }

    override fun append(c: Char): Ansi {
        builder.append(c)
        return this
    }

    public inline fun span(text: String, block: Ansi.() -> Unit = {}): Ansi = apply(block).attribute(text).reset()

    private object NoAnsi : Ansi() {

        override fun attribute(attribute: Ansi16): Ansi = this

        override fun attribute(attribute: Ansi256, background: Boolean): Ansi = this

        override fun attribute(attribute: Attribute): Ansi = this

        override fun cursor(row: Int, column: Int): Ansi = this

        override fun cursorToColumn(x: Int): Ansi = this

        override fun cursorUp(y: Int): Ansi = this

        override fun cursorRight(x: Int): Ansi = this

        override fun cursorDown(y: Int): Ansi = this

        override fun cursorLeft(x: Int): Ansi = this

        override fun cursorDownLine(): Ansi = this

        override fun cursorDownLine(n: Int): Ansi = this

        override fun cursorUpLine(): Ansi = this

        override fun cursorUpLine(n: Int): Ansi = this

        override fun eraseScreen(): Ansi = this

        override fun eraseScreen(kind: Erase): Ansi = this

        override fun eraseLine(): Ansi = this

        override fun eraseLine(kind: Erase): Ansi = this

        override fun scrollUp(rows: Int): Ansi = this

        override fun scrollDown(rows: Int): Ansi = this

        override fun saveCursorPosition(): Ansi = this

        override fun restoreCursorPosition(): Ansi = this

        override fun reset(): Ansi = this
    }

    public companion object {

        private const val FIRST_ESC_CHAR = 27.toChar()
        private const val SECOND_ESC_CHAR = '['

        public fun ansi(): Ansi = if (supportsAnsi()) Ansi() else NoAnsi

        public fun ansi(builder: StringBuilder): Ansi = if (supportsAnsi()) Ansi(builder) else NoAnsi

        public fun ansi(size: Int): Ansi = if (supportsAnsi()) Ansi(size) else NoAnsi
    }
}

public fun buildStringAnsi(block: Ansi.() -> Unit): String = Ansi.ansi().apply(block).toString()

public inline fun String.ansiSpan(block: Ansi.() -> Unit = {}): String = Ansi.ansi().span(this, block).toString()
