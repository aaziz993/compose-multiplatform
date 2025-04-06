package gradle.plugins.kotlin.targets.web.tasks

import org.gradle.kotlin.dsl.withType
import gradle.api.provider.tryAssign
import gradle.api.tasks.applyTo
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBinaryMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

internal abstract class KotlinJsIrLink<T : org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>
    : Kotlin2JsCompile<T>() {

    abstract val incrementalJsIr: Boolean?

    abstract val incrementalWasm: Boolean?

    // Incremental stuff of link task is inside compiler

    abstract val mode: KotlinJsBinaryMode?

    // Do not change the visibility - the property could be used outside
    abstract val rootCacheDirectory: String?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::incrementalJsIr trySet incrementalJsIr
        receiver::incrementalWasm trySet incrementalWasm
        receiver::mode trySet mode
        receiver.rootCacheDirectory tryAssign rootCacheDirectory?.let(project.layout.projectDirectory::dir)
    }
}

@Serializable
@SerialName(" KotlinJsIrLink")
internal data class KotlinJsIrLinkImpl(
    override val incrementalJsIr: Boolean? = null,
    override val incrementalWasm: Boolean? = null,
    override val mode: KotlinJsBinaryMode? = null,
    override val rootCacheDirectory: String? = null,
    override val compilerOptions: KotlinCommonCompilerOptions<KotlinJsCompilerOptions>? = null,
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
    override val kotlinDaemonJvmArguments: List<String>? = null,
    override val setKotlinDaemonJvmArguments: List<String>? = null,
    override val compilerExecutionStrategy: KotlinCompilerExecutionStrategy? = null,
    override val useDaemonFallbackStrategy: Boolean? = null,
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
    override val produceUnpackagedKlib: Boolean? = null,
) : KotlinJsIrLink<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>())
}
