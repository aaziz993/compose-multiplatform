package klib.data.type.ansi

public class NoAnsi : Ansi {
    public constructor(builder: StringBuilder) : super(builder)

    public constructor(size: Int) : super(size)

    override fun fg(color: Int): Ansi = this

    override fun fgRgb(r: Int, g: Int, b: Int): Ansi = this

    override fun bg(color: Int): Ansi = this

    override fun bgRgb(r: Int, g: Int, b: Int): Ansi = this

    override fun ansiIndex(ansiIndex: HasIndex): Ansi = this

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