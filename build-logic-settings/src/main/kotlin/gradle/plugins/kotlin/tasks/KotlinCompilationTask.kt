package gradle.plugins.kotlin.tasks

import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerOptions
import gradle.plugins.kotlin.KotlinCommonCompilerOptionsImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents a Kotlin task compiling using configurable [compilerOptions].
 *
 * See [gradle.plugins.kotlin.KotlinCommonCompilerOptions] and its inheritors for possible compiler options.
 *
 * @see [gradle.plugins.kotlin.KotlinCommonCompilerOptions]
 */
internal interface KotlinCompilationTask<
    T : org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<CO>,
    CO : org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
    > : Task<T> {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: KotlinCommonCompilerOptions<CO>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        compilerOptions?.applyTo(receiver.compilerOptions)
    }
}

@Serializable
@SerialName("KotlinCompilationTask")
internal data class KotlinCompilationTaskImpl(
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
) : KotlinCompilationTask<
    org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>,
    org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions,
    > {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>())
}
