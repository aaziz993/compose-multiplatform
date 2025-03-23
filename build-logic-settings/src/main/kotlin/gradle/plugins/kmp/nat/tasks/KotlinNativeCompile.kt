package gradle.plugins.kmp.nat.tasks

import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.nat.CompilerPluginOptions
import gradle.plugins.kmp.nat.KotlinCompilerPluginData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * A task producing a klibrary from a compilation.
 */
internal abstract class KotlinNativeCompile<T : org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile> :
    AbstractKotlinNativeCompile<T>(),
    KotlinNativeCompileTask<T>,
    K2MultiplatformCompilationTask<T, org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions> {

    // these sources are normally a subset of `source` ones which are already tracked
    abstract val commonSources: Set<String>?
    abstract val setCommonSources: Set<String>?

    context(Project)
    override fun applyTo(recipient: T) {
        super<AbstractKotlinNativeCompile>.applyTo(recipient)
        super<KotlinNativeCompileTask>.applyTo(recipient)
        super<K2MultiplatformCompilationTask>.applyTo(recipient)

        commonSources?.toTypedArray()?.let(recipient.commonSources::from)
        setCommonSources?.let(recipient.commonSources::setFrom)
    }
}

/**
 * A task producing a klibrary from a compilation.
 */
@Serializable
@SerialName("KotlinNativeCompile")
internal data class KotlinNativeCompileImpl(
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
    override val name: String? = null,
    override val produceUnpackagedKlib: Boolean? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val commonSources: Set<String>? = null,
    override val setCommonSources: Set<String>? = null,
) : KotlinNativeCompile<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>())
}
