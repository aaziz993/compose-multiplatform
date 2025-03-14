package gradle.plugins.kmp.nat

import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.gradle.kotlin.dsl.withType

/**
 * A task producing a klibrary from a compilation.
 */
@Serializable
internal data class KotlinNativeCompile(
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
    override val name: String = "",
    override val produceUnpackagedKlib: Boolean? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    // these sources are normally a subset of `source` ones which are already tracked
    val commonSources: List<String>? = null,
) : AbstractKotlinNativeCompile(),
    KotlinNativeCompileTask,
    K2MultiplatformCompilationTask {

    context(Project)
    override fun applyTo(named: Named) {
        super<AbstractKotlinNativeCompile>.applyTo(named)
        super<KotlinNativeCompileTask>.applyTo(named)
        super<K2MultiplatformCompilationTask>.applyTo(named)

        named as KotlinNativeCompile

        commonSources?.let(named.commonSources::setFrom)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KotlinNativeCompile>())
}
