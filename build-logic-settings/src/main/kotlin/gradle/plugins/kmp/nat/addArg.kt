package gradle.plugins.kmp.nat

import gradle.api.tasks.ProducesKlib
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi

internal abstract class AbstractKotlinNativeCompile : AbstractKotlinCompileTool(), ProducesKlib {

    abstract val compilerPluginOptions: CompilerPluginOptions?

//  abstract  val compilerPluginCommandLine

    abstract val compilerPluginClasspath: List<String>?

    abstract val kotlinPluginData: KotlinCompilerPluginData?

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    override fun applyTo(named: Named) {
        super<AbstractKotlinCompileTool>.applyTo(named)
        super<ProducesKlib>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile<*, *>

        named::compilerPluginClasspath trySet compilerPluginClasspath?.let { files(*it.toTypedArray()) }

        named::kotlinPluginData trySet kotlinPluginData?.toKotlinCompilerPluginData()?.let { kotlinPluginData ->
            provider { kotlinPluginData }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile<*, *>>())
}

@Serializable
@SerialName("AbstractKotlinNativeCompile")
internal data class AbstractKotlinNativeCompileImpl(
    override val compilerPluginOptions: CompilerPluginOptions? = null,
    override val compilerPluginClasspath: List<String>? = null,
    override val kotlinPluginData: KotlinCompilerPluginData? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String, override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    override val produceUnpackagedKlib: Boolean? = null,
) : AbstractKotlinNativeCompile()


