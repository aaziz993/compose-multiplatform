package plugin.project.kotlin.model.language

/**
 * DSL entity with the ability to configure Kotlin compiler options.
 */
internal interface HasConfigurableKotlinCompilerOptions<CO : KotlinCommonCompilerOptions> {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: CO?
}
