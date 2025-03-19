package gradle.plugins.kmp.web

import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface KotlinJsCompile : KotlinCompilationTask<KotlinJsCompilerOptions>

@Serializable
@SerialName("KotlinJsCompile")
internal data class KotlinJsCompileImpl(
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = ""
) : KotlinCompilationTask<KotlinJsCompilerOptions>
