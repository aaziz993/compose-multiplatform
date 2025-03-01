package gradle.compiler.model

public data class Program(
    val functionDeclarations: kotlin.collections.List<FunctionDeclaration>,
    val mainFunction: FunctionDeclaration
)
