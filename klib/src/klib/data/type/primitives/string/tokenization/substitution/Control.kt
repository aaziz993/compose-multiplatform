package klib.data.type.primitives.string.tokenization.substitution

public sealed class Control {
    public object NORMAL : Control()
    public object RETURN : Control()
    public data class BREAK(val label: String? = null) : Control()
    public data class CONTINUE(val label: String? = null) : Control()
}