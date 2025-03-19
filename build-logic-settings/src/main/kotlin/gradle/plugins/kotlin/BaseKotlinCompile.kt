package gradle.plugins.kotlin


import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.nat.CompilerPluginConfig
import gradle.plugins.kmp.nat.CompilerPluginOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents any Kotlin compilation task including common task inputs.
 */
internal interface BaseKotlinCompile : KotlinCompileTool {

    /**
     * Paths to the output directories of the friend modules whose internal declarations should be visible.
     */
    val friendPaths: List<String>?

    /**
     * Kotlin compiler plugins artifacts
     * , such as JAR or class files, that participate in the compilation process. All files that are permitted in the [JVM classpath](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/classpath.html) are permitted here.
     */
    val pluginClasspath: List<String>?

    /**
     * The configuration for the Kotlin compiler plugin added in [pluginClasspath] using [CompilerPluginConfig].
     */
    val pluginOptions: List<CompilerPluginOptions>?

    // Exists only to be used in 'KotlinCompileCommon' task.
    // Should be removed once 'moduleName' will be moved into CommonCompilerArguments
    /**
     * @suppress
     */

    val moduleName: String?

    /**
     * Specifies the name of [org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet] that is compiled.
     */
    val sourceSetName: String?

    /**
     * Enables the [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html) flag for compilation.
     */

    val multiPlatformEnabled: Boolean?

    /**
     * Enable more granular tracking of inter-modules as part of incremental compilation. Useful in Android projects.
     */

    val useModuleDetection: Boolean?

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.BaseKotlinCompile

        friendPaths?.let(named.friendPaths::setFrom)
        pluginClasspath?.let(named.pluginClasspath::setFrom)
        named.pluginOptions tryAssign pluginOptions?.map(CompilerPluginConfig::toCompilerPluginConfig)
        named.moduleName tryAssign moduleName
        named.sourceSetName tryAssign sourceSetName
        named.multiPlatformEnabled tryAssign multiPlatformEnabled
        named.useModuleDetection tryAssign useModuleDetection
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.BaseKotlinCompile>())
}

@Serializable
@SerialName("BaseKotlinCompile")
internal data class BaseKotlinCompileImpl(
    override val friendPaths: List<String>? = null,
    override val pluginClasspath: List<String>? = null,
    override val pluginOptions: List<CompilerPluginOptions>? = null,
    override val moduleName: String? = null,
    override val sourceSetName: String? = null,
    override val multiPlatformEnabled: Boolean? = null,
    override val useModuleDetection: Boolean? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val dependsOn: SortedSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: SortedSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = ""
) : BaseKotlinCompile
