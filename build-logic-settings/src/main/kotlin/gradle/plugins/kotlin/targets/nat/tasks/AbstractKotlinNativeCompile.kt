package gradle.plugins.kotlin.targets.nat.tasks

import gradle.accessors.files
import gradle.api.tasks.ProducesKlib
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import gradle.plugins.kotlin.targets.nat.KotlinCompilerPluginData
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi

internal abstract class AbstractKotlinNativeCompile<T : org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile<*, *>>
    : AbstractKotlinCompileTool<T>(), ProducesKlib<T> {

    abstract val compilerPluginOptions: CompilerPluginOptions?

//  abstract  val compilerPluginCommandLine

    abstract val compilerPluginClasspath: Set<String>?
    abstract val setCompilerPluginClasspath: Set<String>?

    abstract val kotlinPluginData: KotlinCompilerPluginData?

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    override fun applyTo(receiver: T) {
        super<AbstractKotlinCompileTool>.applyTo(receiver)
        super<ProducesKlib>.applyTo(receiver)

        receiver::compilerPluginClasspath tryPlus compilerPluginClasspath?.let(project::files)
        receiver::compilerPluginClasspath trySet setCompilerPluginClasspath?.let(project::files)
        receiver::kotlinPluginData trySet kotlinPluginData?.toKotlinCompilerPluginData()?.let { kotlinPluginData ->
            provider { kotlinPluginData }
        }
    }
}

@Serializable
@SerialName("AbstractKotlinNativeCompile")
internal data class AbstractKotlinNativeCompileImpl(
    override val compilerPluginOptions: CompilerPluginOptions? = null,
    override val compilerPluginClasspath: Set<String>? = null,
    override val setCompilerPluginClasspath: Set<String>? = null,
    override val kotlinPluginData: KotlinCompilerPluginData? = null,
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
    override val name: String,
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val produceUnpackagedKlib: Boolean? = null,
) : AbstractKotlinNativeCompile<org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile<*, *>>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile<*, *>>())
}


