package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.collections.list.dropLast
import klib.data.type.collections.replaceAt
import klib.data.type.collections.replaceLast

public data class MachineState(
    val input: (name: String) -> Any? = { null },
    val scopes: List<Map<String, Declaration>> = listOf(emptyMap()),
    val functionScopes: List<Map<String, List<Function>>> = listOf(emptyMap()),
    val log: List<String> = emptyList(),
    val exceptionType: Type? = null,
    val shouldReturn: Boolean = false,
    val result: Any? = null,
) {
    public fun pushScope(): MachineState = copy(
        scopes = scopes + emptyMap(),
        functionScopes = functionScopes + emptyMap()
    )

    public fun popScope(): MachineState {
        require(scopes.size > 1) { "Cannot pop global scope" }

        return copy(
            scopes = scopes.dropLast(),
            functionScopes = functionScopes.dropLast()
        )
    }

    public fun declare(name: String, value: Any?, type: Type, mutable: Boolean): MachineState = copy(
        scopes = scopes.replaceLast {
            require(name !in this) { "Variable '$name' is already declared in this scope" }

            type check value

            this + (name to Declaration(type, mutable, value))
        },
        result = Unit
    )

    public fun getDeclared(name: String): Declaration =
        findDeclaration(name) ?: throw IllegalArgumentException("Undeclared variable '$name'")

    public operator fun set(name: String, value: Any?): MachineState {
        val index = scopes.indexOfLast { scope -> name in scope }
        require(index != -1) { "Undeclared variable '$name'" }

        val declared = scopes[index][name]!!
        require(declared.mutable) { "Cannot assign to immutable val '$name'" }

        declared.type check value

        return copy(
            scopes = scopes.replaceAt(index) { this + (name to declared.copy(value = value)) },
            result = Unit
        )
    }

    public operator fun get(name: String): Any? = findDeclaration(name).let { declaration ->
        if (declaration == null) input(name) else declaration.value
    }

    public fun declareFunction(function: Function): MachineState = copy(
        functionScopes = functionScopes.replaceLast {
            val overloads = this[function.name].orEmpty()

            val typeSignature = function.typeSignature()

            require(overloads.none { overload -> overload.typeSignature() == typeSignature }) {
                "Function '${function.name}(${function.parameters})' is already declared in this scope"
            }

            this + (function.name to (overloads + function))
        },
        result = Unit
    )

    public fun getFunction(name: String, argTypes: List<Type>): Function =
        functionScopes.asReversed().firstNotNullOfOrNull { overloads ->
            val matches = overloads[name]?.filter { function ->
                function.parameters.size == argTypes.size &&
                        function.parameters.zip(argTypes)
                            .all { (parameter, type) -> parameter.type isAssignableFrom type }
            }.orEmpty()

            when (matches.size) {
                0 -> null
                1 -> matches.first()
                else -> {
                    val best = matches.maxWithOrNull(
                        compareBy<Function> { function -> specificityScore(function, argTypes) }
                            .thenBy { f -> exactCount(f, argTypes) }
                    )!!

                    val bestScore = specificityScore(best, argTypes) + exactCount(best, argTypes)

                    val equallyGood = matches.filter { function ->
                        specificityScore(function, argTypes) + exactCount(function, argTypes) == bestScore
                    }

                    require(equallyGood.size == 1) {
                        "Ambiguous call to $name($argTypes), candidates: $equallyGood"
                    }

                    best
                }
            }
        } ?: error("Unresolved function $name($argTypes)")

    private fun findDeclaration(name: String): Declaration? =
        scopes.asReversed().firstNotNullOfOrNull { scopes -> scopes[name] }

    private fun Function.typeSignature(): List<Type> = parameters.map(Variable::type)

    private fun specificityScore(function: Function, argTypes: List<Type>): Int =
        function.parameters.zip(argTypes).sumOf { (parameter, type) ->
            when {
                parameter.type == type -> 2
                type isSubtypeOf parameter.type -> 1
                else -> 0
            }
        }

    private fun exactCount(function: Function, argTypes: List<Type>): Int =
        function.parameters.zip(argTypes).count { (parameter, type) -> parameter.type == type }
}
