@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kmp.nat.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KotlinNativeCompileTask<T : org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompileTask>
    : KotlinCompilationTask<T, org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions> {

}

@Serializable
@SerialName("KotlinNativeCompileTask")
internal data class KotlinNativeCompileTaskImpl(
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
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
) : KotlinNativeCompileTask<org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompileTask> {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompileTask>())
}
