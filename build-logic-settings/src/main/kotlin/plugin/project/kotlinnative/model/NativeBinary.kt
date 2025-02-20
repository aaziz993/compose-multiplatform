package plugin.project.kotlinnative.model

internal interface NativeBinary {

    val baseName: String?

    // Configuration DSL.
    val debuggable: Boolean?
    val optimized: Boolean?

    /** Additional options passed to the linker by the Kotlin/Native compiler. */
    val linkerOpts: List<String>?

    val binaryOptions: Map<String, String>?

    /** Additional arguments passed to the Kotlin/Native compiler. */
    val freeCompilerArgs: List<String>?

    // Output access.
    // TODO: Provide output configurations and integrate them with Gradle Native.
    val outputDirectory: String?

    val outputDirectoryProperty: String?
}
