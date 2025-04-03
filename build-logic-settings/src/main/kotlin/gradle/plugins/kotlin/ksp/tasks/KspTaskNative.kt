package gradle.plugins.kotlin.ksp.tasks

import com.google.devtools.ksp.gradle.KspTaskNative
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import gradle.plugins.kotlin.SubpluginOption
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import gradle.plugins.kotlin.targets.nat.KotlinCompilerPluginData
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompile
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KspTaskNative(
    override val commonSources: Set<String>? = null,
    override val setCommonSources: Set<String>? = null,
    override val compilerPluginOptions: CompilerPluginOptions? = null,
    override val compilerPluginClasspath: Set<String>? = null,
    override val setCompilerPluginClasspath: Set<String>? = null,
    override val kotlinPluginData: KotlinCompilerPluginData? = null,
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
    override val produceUnpackagedKlib: Boolean? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val options: List<SubpluginOption>? = null,
    override val setOptions: List<SubpluginOption>? = null,
    override val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    override val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
) : KotlinNativeCompile<KspTaskNative>(), KspTask<KspTaskNative> {

    context(Project)
    override fun applyTo(receiver: KspTaskNative) {
        super<KotlinNativeCompile>.applyTo(receiver)
        super<KspTask>.applyTo(receiver)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KspTaskNative>())
}
