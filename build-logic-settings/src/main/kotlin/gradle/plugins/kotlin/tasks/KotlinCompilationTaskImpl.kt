package gradle.plugins.kotlin.tasks

import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("KotlinCompilationTask")
internal data class KotlinCompilationTaskImpl<out CO : KotlinCommonCompilerOptions>(
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
    override val compilerOptions: CO? = null,
    override val name: String = "",
) : KotlinCompilationTask<CO>
