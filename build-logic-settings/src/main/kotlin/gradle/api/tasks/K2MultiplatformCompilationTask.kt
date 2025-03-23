@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.api.tasks

import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions

/**
 * Analogous to [KotlinCompilationTask] for K2
 * This does not extend [KotlinCompilationTask], since [KotlinCompilationTask] carries an unwanted/conflicting
 * type parameter `<out T : KotlinCommonOptions>`
 */
internal interface K2MultiplatformCompilationTask<
    T : org.jetbrains.kotlin.gradle.tasks.K2MultiplatformCompilationTask> : Task<T> {

    val compilerOptions: KotlinCommonCompilerOptions<org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions>?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        compilerOptions?.applyTo(recipient.compilerOptions)
    }
}

@Serializable
@SerialName("K2MultiplatformCompilationTask")
internal data class K2MultiplatformCompilationTaskImpl(
    override val compilerOptions: KotlinCommonCompilerOptionsImpl? = null,
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
) : K2MultiplatformCompilationTask<
    org.jetbrains.kotlin.gradle.tasks.K2MultiplatformCompilationTask,
    org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions,
    > {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.K2MultiplatformCompilationTask>())
}
