package gradle.plugins.kotlin.targets.web.tasks

import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.api.tasks.ProducesKlib
import gradle.api.tasks.applyTo
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import gradle.plugins.kotlin.targets.web.KotlinJsCompilerOptions
import gradle.plugins.kotlin.tasks.AbstractKotlinCompile
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

internal abstract class Kotlin2JsCompile<T : org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>
    : AbstractKotlinCompile<T>(),
    KotlinJsCompile<T>,
    K2MultiplatformCompilationTask<T>,
    ProducesKlib<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<K2MultiplatformCompilationTask>.applyTo(receiver)
        super<ProducesKlib>.applyTo(receiver)
    }
}

@Serializable
@SerialName(value = "Kotlin2JsCompile")
internal data class Kotlin2JsCompileImpl(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val incremental: Boolean? = null,
    override val explicitApiMode: ExplicitApiMode? = null,
    override val abiSnapshotRelativePath: String? = null,
    override val name: String? = null,
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val kotlinDaemonJvmArguments: List<String>? = null,
    override val setKotlinDaemonJvmArguments: List<String>? = null,
    override val compilerExecutionStrategy: KotlinCompilerExecutionStrategy? = null,
    override val useDaemonFallbackStrategy: Boolean? = null,
    override val friendPaths: Set<String>? = null,
    override val setFriendPaths: Set<String>? = null,
    override val pluginClasspath: Set<String>? = null,
    override val setPluginClasspath: Set<String>? = null,
    override val pluginOptions: Set<CompilerPluginOptions>? = null,
    override val setPluginOptions: Set<CompilerPluginOptions>? = null,
    override val moduleName: String? = null,
    override val sourceSetName: String? = null,
    override val multiPlatformEnabled: Boolean? = null,
    override val useModuleDetection: Boolean? = null,
    override val produceUnpackagedKlib: Boolean? = null,
) : Kotlin2JsCompile<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>())
}
