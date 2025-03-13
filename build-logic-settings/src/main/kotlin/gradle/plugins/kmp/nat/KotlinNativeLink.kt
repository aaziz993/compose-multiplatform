package gradle.plugins.kmp.nat

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerToolOptions
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import gradle.plugins.kotlin.tasks.KotlinToolTask
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

/**
 * A task producing a final binary from a compilation.
 */
@Serializable
internal data class KotlinNativeLink(
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
    override val toolOptions: KotlinCommonCompilerToolOptions? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    // This property can't be accessed in the execution phase
    val binary: NativeBinaryImpl? = null,
    val apiFiles: List<String>? = null,
    val compilerPluginOptions: CompilerPluginOptions? = null,
    val compilerPluginClasspath: List<String>? = null,
    /**
     * Plugin Data provided by [KpmCompilerPlugin]
     */
    val kotlinPluginData: KotlinCompilerPluginData? = null,
) : AbstractKotlinCompileTool(), KotlinToolTask<KotlinCommonCompilerToolOptions> {

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    override fun applyTo(named: Named) {
        super<AbstractKotlinCompileTool>.applyTo(named)
        super<KotlinToolTask>.applyTo(named)

        named as KotlinNativeLink

        binary?.applyTo(named.binary)
        apiFiles?.let(named.apiFiles::setFrom)
        compilerPluginOptions?.applyTo(named.compilerPluginOptions)
        named::compilerPluginClasspath trySet compilerPluginClasspath?.let { files(*it.toTypedArray()) }
        named::kotlinPluginData trySet kotlinPluginData?.toKotlinCompilerPluginData()?.let { kotlinPluginData ->
            provider { kotlinPluginData }
        }
    }
}
