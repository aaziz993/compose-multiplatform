@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.model

import gradle.model.kotlin.KotlinCommonCompilerOptions
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

/**
 * Analogous to [KotlinCompilationTask] for K2
 * This does not extend [KotlinCompilationTask], since [KotlinCompilationTask] carries an unwanted/conflicting
 * type parameter `<out T : KotlinCommonOptions>`
 */
internal interface K2MultiplatformCompilationTask : Task {

    val compilerOptions: KotlinCommonCompilerOptions?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.K2MultiplatformCompilationTask

        compilerOptions?.applyTo(named.compilerOptions)
    }
}

@Serializable
@SerialName("K2MultiplatformCompilationTask")
internal data class K2MultiplatformCompilationTaskImpl(
    override val compilerOptions: KotlinCommonCompilerOptions? = null,
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
    override val name: String = "",
) : K2MultiplatformCompilationTask
