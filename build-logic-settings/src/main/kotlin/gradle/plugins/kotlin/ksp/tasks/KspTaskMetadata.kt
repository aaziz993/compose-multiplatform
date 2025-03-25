package gradle.plugins.kotlin.ksp.tasks

import com.google.devtools.ksp.gradle.KspTaskMetadata
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.nat.CompilerPluginOptions
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl
import gradle.plugins.kotlin.SubpluginOption
import gradle.plugins.kotlin.tasks.KotlinCompileCommon
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

@Serializable
internal data class KspTaskMetadata(
    override val compilerOptions: KotlinCommonCompilerOptionsImpl? = null,
    override val incremental: Boolean? = null,
    override val explicitApiMode: ExplicitApiMode? = null,
    override val abiSnapshotRelativePath: String? = null,
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
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
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
    override val options: List<SubpluginOption>? = null,
    override val setOptions: List<SubpluginOption>? = null,
    override val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    override val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
) : KotlinCompileCommon<KspTaskMetadata>(), KspTask<KspTaskMetadata> {

    context(Project)
    override fun applyTo(receiver: KspTaskMetadata) {
        super<KotlinCompileCommon>.applyTo(receiver)
        super<KspTask>.applyTo(receiver)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KspTaskMetadata>())
}
