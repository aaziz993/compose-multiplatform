package gradle.plugins.kotlin.tasks

import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.nat.CompilerPluginOptions
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

internal abstract class AbstractKotlinCompile<T : org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile<*>>
    : AbstractKotlinCompileTool<T>(), BaseKotlinCompile<T> {

    abstract val compilerOptions: KotlinCommonCompilerOptionsImpl?

    // indicates that task should compile kotlin incrementally if possible
    // it's not possible when IncrementalTaskInputs#isIncremental returns false (i.e first build)
    // todo: deprecate and remove (we may need to design api for configuring IC)
    // don't rely on it to check if IC is enabled, use isIncrementalCompilationEnabled instead

    abstract val incremental: Boolean?

    abstract val explicitApiMode: ExplicitApiMode?

    abstract val abiSnapshotRelativePath: String?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<AbstractKotlinCompileTool>.applyTo(receiver)
        super<BaseKotlinCompile>.applyTo(receiver)

        compilerOptions?.applyTo(receiver.compilerOptions)
        receiver::incremental trySet incremental
        receiver.explicitApiMode tryAssign explicitApiMode
        receiver.abiSnapshotRelativePath tryAssign abiSnapshotRelativePath
    }
}

@Serializable
@SerialName("AbstractKotlinCompile")
internal data class AbstractKotlinCompileImpl(
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
) : AbstractKotlinCompile<org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile<*>>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile<*>>())
}
