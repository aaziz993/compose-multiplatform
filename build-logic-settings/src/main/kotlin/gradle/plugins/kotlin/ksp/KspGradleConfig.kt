package gradle.plugins.kotlin.ksp

import com.google.devtools.ksp.gradle.KspGradleConfig
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.provider.tryAddAll
import gradle.api.provider.tryAssign
import gradle.api.provider.tryPutAll
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

@Serializable
internal data class KspGradleConfig(
    val processorClasspath: Set<String>? = null,
    val setProcessorClasspath: Set<String>? = null,
    val moduleName: String? = null,
    val sourceRoots: Set<String>? = null,
    val setSourceRoots: Set<String>? = null,
    val commonSourceRoots: Set<String>? = null,
    val setCommonSourceRoots: Set<String>? = null,
    val javaSourceRoots: Set<String>? = null,
    val setJavaSourceRoots: Set<String>? = null,
    val libraries: Set<String>? = null,
    val setLibraries: Set<String>? = null,
    val jdkHome: String? = null,
    val projectBaseDir: String? = null,
    val outputBaseDir: String? = null,
    val cachesDir: String? = null,
    val kotlinOutputDir: String? = null,
    val javaOutputDir: String? = null,
    val classOutputDir: String? = null,
    val resourceOutputDir: String? = null,
    val languageVersion: String? = null,
    val apiVersion: String? = null,
    val processorOptions: Map<String, String>? = null,
    val setProcessorOptions: Map<String, String>? = null,
    // Unfortunately, passing project.logger over is not possible.
    val logLevel: LogLevel? = null,
    val allWarningsAsErrors: Boolean? = null,
    val excludedProcessors: Set<String>? = null,
    val setExcludedProcessors: Set<String>? = null,
    val jvmTarget: String? = null,
    val jvmDefaultMode: String? = null,
    val incremental: Boolean? = null,
    val incrementalLog: Boolean? = null,
    val classpathStructure: Set<String>? = null,
    val setClasspathStructure: Set<String>? = null,
    val platformType: KotlinPlatformType? = null,
    val konanTargetName: String? = null,
    val konanHome: String? = null,
) {

    context(Project)
    fun applyTo(receiver: KspGradleConfig) {
        receiver.processorClasspath tryFrom processorClasspath
        receiver.processorClasspath trySetFrom setProcessorClasspath
        receiver.moduleName tryAssign moduleName
        receiver.sourceRoots tryFrom sourceRoots
        receiver.sourceRoots trySetFrom setSourceRoots
        receiver.commonSourceRoots tryFrom commonSourceRoots
        receiver.commonSourceRoots trySetFrom setCommonSourceRoots
        receiver.javaSourceRoots tryFrom javaSourceRoots
        receiver.javaSourceRoots trySetFrom setJavaSourceRoots
        receiver.libraries tryFrom libraries
        receiver.libraries trySetFrom setLibraries
        receiver.jdkHome tryAssign jdkHome?.let(project::file)
        receiver.projectBaseDir tryAssign projectBaseDir?.let(project::file)
        receiver.outputBaseDir tryAssign outputBaseDir?.let(project::file)
        receiver.cachesDir tryAssign cachesDir?.let(project.layout.projectDirectory::dir)
        receiver.kotlinOutputDir tryAssign kotlinOutputDir?.let(project::file)
        receiver.javaOutputDir tryAssign javaOutputDir?.let(project::file)
        receiver.classOutputDir tryAssign classOutputDir?.let(project::file)
        receiver.resourceOutputDir tryAssign resourceOutputDir?.let(project::file)
        receiver.languageVersion tryAssign languageVersion
        receiver.apiVersion tryAssign apiVersion
        receiver.processorOptions tryPutAll processorOptions
        receiver.processorOptions tryAssign setProcessorOptions
        // Unfortunately, passing project.logger over is not possible.
        receiver.logLevel tryAssign logLevel
        receiver.allWarningsAsErrors tryAssign allWarningsAsErrors
        receiver.excludedProcessors tryAddAll excludedProcessors
        receiver.excludedProcessors tryAssign setExcludedProcessors
        receiver.jvmTarget tryAssign jvmTarget
        receiver.jvmDefaultMode tryAssign jvmDefaultMode
        receiver.incremental tryAssign incremental
        receiver.incrementalLog tryAssign incrementalLog
        receiver.classpathStructure tryFrom classpathStructure
        receiver.classpathStructure trySetFrom setClasspathStructure
        receiver.platformType tryAssign platformType
        receiver.konanTargetName tryAssign konanTargetName
        receiver.konanHome tryAssign konanHome
    }
}
