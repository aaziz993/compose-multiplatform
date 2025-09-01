package klib.data.type.primitives.string.tokenization.evaluation.program

public data class Function(
    val name: String,
    val parameters: List<Variable>,
    val body: Statement
) {
    override fun hashCode(): Int = klib.data.type.hashCode(name, *parameters.toTypedArray())

    override fun equals(other: Any?): Boolean =
        other is Function && other.name == name && other.parameters == parameters

    override fun toString(): String = "$name($parameters)"
}