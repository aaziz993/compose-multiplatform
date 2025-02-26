package plugin.project.kotlin.model.language.nat

import gradle.namedOrAll
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import plugin.model.dependency.ProjectDependency
import plugin.project.kotlin.model.language.KotlinCompilation
import plugin.project.kotlin.model.language.KotlinCompilationOutput
import plugin.project.kotlin.model.language.KotlinSourceSet

@Serializable
internal data class KotlinNativeCompilation(
    override val compilationName: String,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<ProjectDependency>? = null,
    // Interop DSL.
    val cinterops: List<DefaultCInteropSettings>? = null
) : KotlinCompilation {

    context(Project)
    fun applyTo(compilation: KotlinNativeCompilation) {
        super.applyTo(compilation)
        cinterops?.forEach { cinterops ->
            compilation.cinterops.namedOrAll(cinterops.name) {
                cinterops.applyTo(this)

            }
        }
    }
}
