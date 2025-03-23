package gradle.plugins.kotlin.ksp

import com.google.devtools.ksp.gradle.KspGradleConfig
import gradle.api.tryAssign
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
    fun applyTo(recipient: KspGradleConfig) {
        processorClasspath?.toTypedArray()?.let(recipient.processorClasspath::from)
        setProcessorClasspath?.let(recipient.processorClasspath::setFrom)
        recipient.moduleName tryAssign moduleName
        sourceRoots?.toTypedArray()?.let(recipient.sourceRoots::from)
        setSourceRoots?.let(recipient.sourceRoots::setFrom)
        commonSourceRoots?.toTypedArray()?.let(recipient.commonSourceRoots::from)
        setCommonSourceRoots?.let(recipient.commonSourceRoots::setFrom)
        javaSourceRoots?.toTypedArray()?.let(recipient.javaSourceRoots::from)
        setJavaSourceRoots?.let(recipient.javaSourceRoots::setFrom)
        libraries?.toTypedArray()?.let(recipient.libraries::from)
        setLibraries?.let(recipient.libraries::setFrom)
        recipient.jdkHome tryAssign jdkHome?.let(::file)
        recipient.projectBaseDir tryAssign projectBaseDir?.let(::file)
        recipient.outputBaseDir tryAssign outputBaseDir?.let(::file)
        recipient.cachesDir tryAssign cachesDir?.let(layout.projectDirectory::dir)
        recipient.kotlinOutputDir tryAssign kotlinOutputDir?.let(::file)
        recipient.javaOutputDir tryAssign javaOutputDir?.let(::file)
        recipient.classOutputDir tryAssign classOutputDir?.let(::file)
        recipient.resourceOutputDir tryAssign resourceOutputDir?.let(::file)
        recipient.languageVersion tryAssign languageVersion
        recipient.apiVersion tryAssign apiVersion

        recipient.processorOptions tryAssign processorOptions?.let { processorOptions ->
            recipient.processorOptions.get() + processorOptions
        }

        recipient.processorOptions tryAssign setProcessorOptions
        // Unfortunately, passing project.logger over is not possible.
        recipient.logLevel tryAssign logLevel
        recipient.allWarningsAsErrors tryAssign allWarningsAsErrors

        recipient.excludedProcessors tryAssign excludedProcessors?.let { excludedProcessors ->
            recipient.excludedProcessors.get() + excludedProcessors
        }

        recipient.excludedProcessors tryAssign setExcludedProcessors
        recipient.jvmTarget tryAssign jvmTarget
        recipient.jvmDefaultMode tryAssign jvmDefaultMode
        recipient.incremental tryAssign incremental
        recipient.incrementalLog tryAssign incrementalLog
        classpathStructure?.toTypedArray()?.let(recipient.classpathStructure::from)
        setClasspathStructure?.let(recipient.classpathStructure::setFrom)
        recipient.platformType tryAssign platformType
        recipient.konanTargetName tryAssign konanTargetName
        recipient.konanHome tryAssign konanHome
    }
}
