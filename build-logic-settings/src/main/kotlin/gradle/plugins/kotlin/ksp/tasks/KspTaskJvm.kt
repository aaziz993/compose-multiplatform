package gradle.plugins.kotlin.ksp.tasks

import com.google.devtools.ksp.gradle.KspTaskJvm
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import gradle.plugins.kotlin.tasks.KotlinCompile
import gradle.plugins.kotlin.tasks.KotlinJavaToolchain
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode

@Serializable
internal data class KspTaskJvm(
    override val javaPackagePrefix: String? = null,
    override val usePreciseJavaTracking: Boolean? = null,
    override val useKotlinAbiSnapshot: Boolean? = null,
    override val classpathSnapshotProperties: ClasspathSnapshotProperties? = null,
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
    override val compilerOptions: KotlinCommonCompilerOptions<KotlinJvmCompilerOptions>? = null,
    override val jvmTargetValidationMode: JvmTargetValidationMode? = null,
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
    override val kotlinJavaToolchain: KotlinJavaToolchain? = null,
    val destination: String? = null,
) : KotlinCompile<KspTaskJvm>() {

    context(Project)
    override fun applyTo(receiver: KspTaskJvm) {
        super.applyTo(receiver)

        receiver.destination tryAssign destination?.let(project::file)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KspTaskJvm>())
}
