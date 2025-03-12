package gradle.plugins.kotlin

import gradle.tasks.Task
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents a Kotlin task compiling using configurable [compilerOptions].
 *
 * See [KotlinCommonCompilerOptions] and its inheritors for possible compiler options.
 *
 * @see [KotlinCommonCompilerOptions]
 */
internal interface KotlinCompilationTask<out CO : KotlinCommonCompilerOptions> : Task {

    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    val compilerOptions: CO?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>

        compilerOptions?.applyTo(named.compilerOptions)
    }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>())
}

@Serializable
@SerialName("KotlinCompilationTask")
internal data class KotlinCompilationTaskImpl<out CO : KotlinCommonCompilerOptions>(
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
    override val compilerOptions: CO?=null,
    override val name: String = "",
) : KotlinCompilationTask<CO>
