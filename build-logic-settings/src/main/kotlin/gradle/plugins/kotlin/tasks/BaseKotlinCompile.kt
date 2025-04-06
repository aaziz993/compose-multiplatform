package gradle.plugins.kotlin.tasks

import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.provider.tryAssign
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import gradle.plugins.kotlin.targets.nat.CompilerPluginConfig
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents any Kotlin compilation task including common task inputs.
 */
internal interface BaseKotlinCompile<T : org.jetbrains.kotlin.gradle.tasks.BaseKotlinCompile> : KotlinCompileTool<T> {

    /**
     * Paths to the output directories of the friend modules whose internal declarations should be visible.
     */
    val friendPaths: Set<String>?
    val setFriendPaths: Set<String>?

    /**
     * Kotlin compiler plugins artifacts
     * , such as JAR or class files, that participate in the compilation process. All files that are permitted in the [JVM classpath](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/classpath.html) are permitted here.
     */
    val pluginClasspath: Set<String>?
    val setPluginClasspath: Set<String>?

    /**
     * The configuration for the Kotlin compiler plugin added in [pluginClasspath] using [CompilerPluginConfig].
     */
    val pluginOptions: Set<CompilerPluginOptions>?
    val setPluginOptions: Set<CompilerPluginOptions>?

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
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.friendPaths tryFrom friendPaths
        receiver.friendPaths trySetFrom setFriendPaths
        receiver.pluginClasspath tryFrom pluginClasspath
        receiver.pluginClasspath trySetFrom setPluginClasspath

        receiver.pluginOptions tryAssign pluginOptions
            ?.map(CompilerPluginConfig::toCompilerPluginConfig)
            ?.let { pluginOptions ->
                receiver.pluginOptions.get() + pluginOptions
            }

        receiver.pluginOptions tryAssign setPluginOptions?.map(CompilerPluginConfig::toCompilerPluginConfig)
        receiver.moduleName tryAssign moduleName
        receiver.sourceSetName tryAssign sourceSetName
        receiver.multiPlatformEnabled tryAssign multiPlatformEnabled
        receiver.useModuleDetection tryAssign useModuleDetection
    }
}

@Serializable
@SerialName("BaseKotlinCompile")
internal data class BaseKotlinCompileImpl(
    override val friendPaths: Set<String>? = null,
    override val setFriendPaths: Set<String>? = null,
    override val pluginClasspath: Set<String>? = null,
    override val setPluginClasspath: Set<String>? = null,
    override val pluginOptions: Set<CompilerPluginOptions>? = null,
    override val setPluginOptions: Set<CompilerPluginOptions>? = null,
    override val moduleName: String? = null,
    override val sourceSetName: String? = null,
    override val multiPlatformEnabled: Boolean? = null,
    override val useModuleDetection: Boolean? = null,
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
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
) : BaseKotlinCompile<org.jetbrains.kotlin.gradle.tasks.BaseKotlinCompile> {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.BaseKotlinCompile>())
}
