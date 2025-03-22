package gradle.plugins.kmp.nat


import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerToolOptions
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import gradle.plugins.kotlin.tasks.KotlinToolTask
import kotlinx.serialization.Serializable
import org.gradle.api.Named
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
    override val name: String? = null,,
    override val toolOptions: KotlinCommonCompilerToolOptions? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
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
    override fun applyTo(recipient: T) {
        super<AbstractKotlinCompileTool>.applyTo(named)
        super<KotlinToolTask>.applyTo(named)

        named as KotlinNativeLink

        binary?.applyTo(named.binary)
        apiFiles?.toTypedArray()?.let(named.apiFiles::from)
setApiFiles?.let(named.apiFiles::setFrom)
        compilerPluginOptions?.applyTo(named.compilerPluginOptions)
        named::compilerPluginClasspath trySet compilerPluginClasspath?.toTypedArray()?.let(::files)
        named::kotlinPluginData trySet kotlinPluginData?.toKotlinCompilerPluginData()?.let { kotlinPluginData ->
            provider { kotlinPluginData }
        }
    }
}
