@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kmp.nat

import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KotlinNativeCompileTask : KotlinCompilationTask<KotlinNativeCompilerOptions> {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompileTask>())
}

@Serializable
@SerialName("KotlinNativeCompileTask")
internal data class KotlinNativeCompileTaskImpl(
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
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
) : KotlinNativeCompileTask
