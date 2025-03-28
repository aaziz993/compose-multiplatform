package gradle.plugins.kotlin.targets.nat

import gradle.accessors.files
import gradle.api.tasks.applyTo
import gradle.api.tryFrom
import gradle.api.tryPlus
import gradle.api.trySet
import gradle.api.trySetFrom
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerToolOptionsImpl
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import gradle.plugins.kotlin.tasks.KotlinToolTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

/**
 * A task producing a final binary from a compilation.
 */
@Serializable
internal data class KotlinNativeLink(
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
    override val toolOptions: KotlinCommonCompilerToolOptionsImpl? = null,
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    // This property can't be accessed in the execution phase
    val binary: NativeBinaryImpl? = null,
    val apiFiles: Set<String>? = null,
    val setApiFiles: Set<String>? = null,
    val compilerPluginOptions: CompilerPluginOptions? = null,
    val compilerPluginClasspath: Set<String>? = null,
    val setCompilerPluginClasspath: Set<String>? = null,
    /**
     * Plugin Data provided by [KpmCompilerPlugin]
     */
    val kotlinPluginData: KotlinCompilerPluginData? = null,
) : AbstractKotlinCompileTool<KotlinNativeLink>(),
    KotlinToolTask<KotlinNativeLink, org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions> {

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    override fun applyTo(receiver: KotlinNativeLink) {
        super<AbstractKotlinCompileTool>.applyTo(receiver)
        super<KotlinToolTask>.applyTo(receiver)

        binary?.applyTo(receiver.binary)
        receiver.apiFiles tryFrom apiFiles
        receiver.apiFiles trySetFrom setApiFiles
        compilerPluginOptions?.applyTo(receiver.compilerPluginOptions)
        receiver::compilerPluginClasspath tryPlus compilerPluginClasspath?.let(project::files)
        receiver::compilerPluginClasspath trySet setCompilerPluginClasspath?.let(project::files)
        receiver::kotlinPluginData trySet kotlinPluginData?.toKotlinCompilerPluginData()?.let { kotlinPluginData ->
            provider { kotlinPluginData }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KotlinNativeLink>())
}
