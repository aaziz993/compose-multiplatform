package plugin.project.kotlin.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

/**
 * Represents a Kotlin task compiling using configurable [compilerOptions].
 *
 * See [KotlinCommonCompilerOptions] and its inheritors for possible compiler options.
 *
 * @see [KotlinCommonCompilerOptions]
 */
@Serializable
internal data class KotlinCompilationTask<out CO : KotlinCommonCompilerOptions>(
    override val name: String = "",
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: CO?
) : Task {

    context(Project)
    override fun applyTo(_tasks: NamedDomainObjectContainer<org.gradle.api.Task>) {
        super.applyTo(_tasks)

        _tasks.configure {
            this as KotlinCompilationTask<*>

            this@KotlinCompilationTask.compilerOptions?.applyTo(compilerOptions as KotlinCommonCompilerToolOptions)
        }
    }
}
