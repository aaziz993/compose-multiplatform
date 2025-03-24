package gradle.plugins.kmp.web

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KotlinJsCompile<T : org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>
    : KotlinCompilationTask<T, org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions>

@Serializable
@SerialName("KotlinJsCompile")
internal data class KotlinJsCompileImpl(
    override val compilerOptions: KotlinJsCompilerOptions? = null,
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
) : KotlinJsCompile<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile> {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>())
}
