package gradle.api.tasks.compile

import gradle.accessors.files
import gradle.api.provider.tryAssign
import klib.data.type.reflection.trySet
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
        receiver::setFailOnError trySet failOnError
        receiver::setVerbose trySet verbose
        receiver::setListFiles trySet listFiles
        receiver::setDeprecation trySet deprecation
        receiver::setWarnings trySet warnings
        receiver::setEncoding trySet encoding
        receiver::setDebug trySet debug
        receiver::setFork trySet fork
        receiver::setBootstrapClasspath trySet bootstrapClasspath?.let(project::files)
        receiver::setExtensionDirs trySet extensionDirs
        receiver::setCompilerArgs trySet compilerArgs
        receiver::setIncremental trySet incremental
        receiver::setSourcepath trySet sourcepath?.let(project::files)
        receiver::setAnnotationProcessorPath trySet annotationProcessorPath?.let(project::files)
        receiver.incrementalAfterFailure tryAssign incrementalAfterFailure
        receiver.javaModuleVersion tryAssign javaModuleVersion
        receiver.javaModuleMainClass tryAssign javaModuleMainClass
        receiver.release tryAssign release
        receiver.generatedSourceOutputDirectory tryAssign generatedSourceOutputDirectory?.let(project.layout.projectDirectory::dir)
        receiver.headerOutputDirectory tryAssign headerOutputDirectory?.let(project.layout.projectDirectory::dir)
    }
}
