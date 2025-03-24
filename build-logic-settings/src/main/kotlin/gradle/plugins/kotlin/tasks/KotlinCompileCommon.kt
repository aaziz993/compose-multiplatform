package gradle.plugins.kotlin.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.nat.CompilerPluginOptions
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions

internal abstract class KotlinCompileCommon<T : org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon>
    : AbstractKotlinCompile<T>(), KotlinCompilationTask<T, KotlinCommonCompilerOptions> {

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<AbstractKotlinCompile>.applyTo(receiver)
        super<KotlinCompilationTask>.applyTo(receiver)
    }
}

@Serializable
@SerialName("KotlinCompileCommon")
internal data class KotlinCompileCommonImpl(
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
) : KotlinCompileCommon<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon>())
}
