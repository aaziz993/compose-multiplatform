package gradle.compiler.model

import java.util.*

public open class FunctionDeclaration(
    public open val name: String,
    public val parameters: kotlin.collections.List<Variable>,
    public open val body: Statement
) {

    override fun hashCode(): Int = Objects.hash(name, parameters)
    override fun equals(other: Any?): Boolean =
        other is FunctionDeclaration && other.name == name && other.parameters == parameters

    override fun toString(): String = "$name(${parameters.joinToString { it.name }})"
}

public data class UnresolvedFunction(override val name: String, val dimensions: Int) : FunctionDeclaration(name, (1..dimensions).map { Variable("unresolved") }, Skip) {

    override val body: Nothing
        get() = throw IllegalStateException("Getting body of an unresolved function $this")
}

public sealed class Intrinsic(name: String, parameters: kotlin.collections.List<Variable>, public val throws: Boolean = false) : FunctionDeclaration(name, parameters, Skip) {

    override val body: Statement get() = throw IllegalStateException("Getting body of an unresolved function $this")

    public object READ : Intrinsic("read", emptyList())
    public object WRITE : Intrinsic("write", listOf(Variable("expression")))
    public object STRMAKE : Intrinsic("strmake", listOf(Variable("n"), Variable("c")))
    public object STRCMP : Intrinsic("strcmp", listOf(Variable("S1"), Variable("S2")))
    public object STRGET : Intrinsic("strget", listOf(Variable("S"), Variable("i")))
    public object STRDUP : Intrinsic("strdup", listOf(Variable("S")))
    public object STRSET : Intrinsic("strset", listOf(Variable("S"), Variable("i"), Variable("c")))
    public object STRCAT : Intrinsic("strcat", listOf(Variable("S1"), Variable("S2")))
    public object STRSUB : Intrinsic("strsub", listOf(Variable("S"), Variable("i"), Variable("j")))
    public object STRLEN : Intrinsic("strlen", listOf(Variable("S")))
    public object ARRMAKE : Intrinsic("arrmake", listOf(Variable("n"), Variable("init")))
    public object ARRMAKEBOX : Intrinsic("Arrmake", listOf(Variable("n"), Variable("Init")))
    public object ARRGET : Intrinsic("arrget", listOf(Variable("A"), Variable("i")))
    public object ARRSET : Intrinsic("arrset", listOf(Variable("A"), Variable("i"), Variable("v")))
    public object ARRLEN : Intrinsic("arrlen", listOf(Variable("A")))

    public companion object {

        public val resolvable: kotlin.collections.List<Intrinsic> by lazy {
            listOf(
                READ, WRITE,
                STRMAKE, STRCMP, STRGET, STRDUP, STRSET, STRCAT, STRSUB, STRLEN,
                ARRMAKE, ARRMAKEBOX, ARRLEN,
            )
        }
    }
}
