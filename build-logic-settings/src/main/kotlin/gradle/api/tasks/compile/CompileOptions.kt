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
    fun applyTo(receiver: CompileOptions) {
        failOnError?.let(receiver::setFailOnError)
        verbose?.let(receiver::setVerbose)
        listFiles?.let(receiver::setListFiles)
        deprecation?.let(receiver::setDeprecation)
        warnings?.let(receiver::setWarnings)
        encoding?.let(receiver::setEncoding)
        debug?.let(receiver::setDebug)
        fork?.let(receiver::setFork)
        bootstrapClasspath?.toTypedArray()?.let(::files)?.let(receiver::setBootstrapClasspath)
        extensionDirs?.let(receiver::setExtensionDirs)
        compilerArgs?.let(receiver::setCompilerArgs)
        incremental?.let(receiver::setIncremental)
        sourcepath?.toTypedArray()?.let(::files).let(receiver::setSourcepath)
        annotationProcessorPath?.toTypedArray()?.let(::files)?.let(receiver::setAnnotationProcessorPath)
        receiver.incrementalAfterFailure tryAssign incrementalAfterFailure
        receiver.javaModuleVersion tryAssign javaModuleVersion
        receiver.javaModuleMainClass tryAssign javaModuleMainClass
        receiver.release tryAssign release
        receiver.generatedSourceOutputDirectory tryAssign generatedSourceOutputDirectory?.let(layout.projectDirectory::dir)
        receiver.headerOutputDirectory tryAssign headerOutputDirectory?.let(layout.projectDirectory::dir)
    }
}
