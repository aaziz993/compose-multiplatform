package gradle.api.tasks.compile

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
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
    val bootstrapClasspath: Set<String>? = null,
    val extensionDirs: String? = null,
    val compilerArgs: List<String>? = null,
    val incremental: Boolean? = null,
    val sourcepath: Set<String>? = null,
    val annotationProcessorPath: Set<String>? = null,
    val incrementalAfterFailure: Boolean? = null,
    val javaModuleVersion: String? = null,
    val javaModuleMainClass: String? = null,
    val release: Int? = null,
    val generatedSourceOutputDirectory: String? = null,
    val headerOutputDirectory: String? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: CompileOptions) {
        failOnError?.let(recipient::setFailOnError)
        verbose?.let(recipient::setVerbose)
        listFiles?.let(recipient::setListFiles)
        deprecation?.let(recipient::setDeprecation)
        warnings?.let(recipient::setWarnings)
        encoding?.let(recipient::setEncoding)
        debug?.let(recipient::setDebug)
        fork?.let(recipient::setFork)
        bootstrapClasspath?.toTypedArray()?.let(::files)?.let(recipient::setBootstrapClasspath)
        extensionDirs?.let(recipient::setExtensionDirs)
        compilerArgs?.let(recipient::setCompilerArgs)
        incremental?.let(recipient::setIncremental)
        sourcepath?.toTypedArray()?.let(::files).let(recipient::setSourcepath)
        annotationProcessorPath?.toTypedArray()?.let(::files)?.let(recipient::setAnnotationProcessorPath)
        recipient.incrementalAfterFailure tryAssign incrementalAfterFailure
        recipient.javaModuleVersion tryAssign javaModuleVersion
        recipient.javaModuleMainClass tryAssign javaModuleMainClass
        recipient.release tryAssign release
        recipient.generatedSourceOutputDirectory tryAssign generatedSourceOutputDirectory?.let(layout.projectDirectory::dir)
        recipient.headerOutputDirectory tryAssign headerOutputDirectory?.let(layout.projectDirectory::dir)
    }
}
