package gradle.tasks.compile

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.compile.CompileOptions

/**
 * Main options for Java compilation.
 */
@Serializable
internal data class CompileOptions(
    /**
     * Tells whether to fail the build when compilation fails. Defaults to `true`.
     */
    val failOnError: Boolean? = null,
    val verbose: Boolean? = null,
    val listFiles: Boolean? = null,
    val deprecation: Boolean? = null,
    val warnings: Boolean? = null,
    val encoding: String? = null,
    val debug: Boolean? = null,
    val fork: Boolean? = null,
    val bootstrapClasspath: List<String>? = null,
    val extensionDirs: String? = null,
    val compilerArgs: List<String>? = null,
    val incremental: Boolean? = null,
    val sourcepath: FileCollection? = null,
    val annotationProcessorPath: List<String>? = null,
    val incrementalAfterFailure: Boolean? = null,
    val javaModuleVersion: String? = null,
    val javaModuleMainClass: String? = null,
    val release: Int? = null,
    val generatedSourceOutputDirectory: String? = null,
    val headerOutputDirectory: String? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(options: CompileOptions) {
        failOnError?.let(options::setFailOnError)
        verbose?.let(options::setVerbose)
        listFiles?.let(options::setListFiles)
        deprecation?.let(options::setDeprecation)
        warnings?.let(options::setWarnings)
        encoding?.let(options::setEncoding)
        debug?.let(options::setDebug)
        fork?.let(options::setFork)

        bootstrapClasspath?.let { bootstrapClasspath ->
            options.bootstrapClasspath = files(*bootstrapClasspath.toTypedArray())
        }

        extensionDirs?.let(options::setExtensionDirs)
        compilerArgs?.let(options::setCompilerArgs)
        incremental?.let(options::setIncremental)
        sourcepath?.let(options::setSourcepath)

        annotationProcessorPath?.let { annotationProcessorPath ->
            options.annotationProcessorPath = files(*annotationProcessorPath.toTypedArray())
        }

        options.incrementalAfterFailure tryAssign incrementalAfterFailure
        options.javaModuleVersion tryAssign javaModuleVersion
        options.javaModuleMainClass tryAssign javaModuleMainClass
        options.release tryAssign release
        options.generatedSourceOutputDirectory tryAssign generatedSourceOutputDirectory?.let(layout.projectDirectory::dir)
        options.headerOutputDirectory tryAssign headerOutputDirectory?.let(layout.projectDirectory::dir)
    }
}
