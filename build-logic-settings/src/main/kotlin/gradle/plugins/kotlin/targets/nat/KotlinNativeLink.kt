package gradle.plugins.kotlin.targets.nat

import gradle.accessors.files
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerToolOptionsImpl
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import gradle.plugins.kotlin.tasks.KotlinToolTask
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi

/**
 * A task producing a final binary from a compilation.
 */
internal abstract class KotlinNativeLink<T : org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>
    : AbstractKotlinCompileTool<T>(),
    KotlinToolTask<T, org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions> {

    // This property can't be accessed in the execution phase
    abstract val binary: NativeBinaryImpl?

    abstract val apiFiles: Set<String>?

    abstract val setApiFiles: Set<String>?

    abstract val compilerPluginOptions: CompilerPluginOptions?

    abstract val compilerPluginClasspath: Set<String>?

    abstract val setCompilerPluginClasspath: Set<String>?

    /**
     * Plugin Data provided by [KpmCompilerPlugin]
     */
    abstract val kotlinPluginData: KotlinCompilerPluginData?

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    override fun applyTo(receiver: T) {
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
}

@Serializable
internal data class KotlinNativeLinkImpl(
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
    override val binary: NativeBinaryImpl? = null,
    override val apiFiles: Set<String>? = null,
    override val setApiFiles: Set<String>? = null,
    override val compilerPluginOptions: CompilerPluginOptions? = null,
    override val compilerPluginClasspath: Set<String>? = null,
    override val setCompilerPluginClasspath: Set<String>? = null,
    override val kotlinPluginData: KotlinCompilerPluginData? = null,
) : KotlinNativeLink<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>())
}
