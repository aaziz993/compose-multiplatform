package gradle.plugins.kmp.nat


import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

/**
 * A task producing a klibrary from a compilation.
 */
@Serializable
internal data class KotlinNativeCompile(
    override val compilerPluginOptions: CompilerPluginOptions? = null,
    override val compilerPluginClasspath: List<String>? = null,
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
    override val name: String? = null,,
    override val produceUnpackagedKlib: Boolean? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    // these sources are normally a subset of `source` ones which are already tracked
    val commonSources: List<String>? = null,
) : AbstractKotlinNativeCompile(),
    KotlinNativeCompileTask,
    K2MultiplatformCompilationTask {

        context(Project)
    override fun applyTo(recipient: T) {
        super<AbstractKotlinNativeCompile>.applyTo(named)
        super<KotlinNativeCompileTask>.applyTo(named)
        super<K2MultiplatformCompilationTask>.applyTo(named)

        named as KotlinNativeCompile

        commonSources?.toTypedArray()?.let(named.commonSources::from)
setCommonSources?.let(named.commonSources::setFrom)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KotlinNativeCompile>())
}
