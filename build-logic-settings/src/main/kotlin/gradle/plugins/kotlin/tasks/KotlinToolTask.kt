package gradle.plugins.kotlin.tasks

import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import klib.data.type.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinCommonCompilerToolOptions
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents a Kotlin task performing further processing of compiled code via additional Kotlin tools using configurable [toolOptions].
 *
 * Check [KotlinCommonCompilerToolOptions] inheritors (excluding [KotlinCommonCompilerToolOptions]) for the possible configuration
 * options.
 *
 * @see [KotlinCommonCompilerToolOptions]
 */
internal interface KotlinToolTask<T : org.jetbrains.kotlin.gradle.tasks.KotlinToolTask<TO>,
    TO : org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions> : Task<T> {

    /**
     * Represents the tool options used by a Kotlin task with reasonable configured defaults.
     *
     * Could be used to either get the values of currently configured options or to modify them.
     */
    val toolOptions: KotlinCommonCompilerToolOptions<TO>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        toolOptions?.applyTo(receiver.toolOptions)
    }
}

@Serializable
internal data class KotlinToolTaskImpl(
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
    override val toolOptions: KotlinCommonCompilerToolOptions<org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions>? = null,
    override val name: String? = null,
) : KotlinToolTask<
    org.jetbrains.kotlin.gradle.tasks.KotlinToolTask<*>,
    org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions,
    > {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinToolTask<*>>())
}
