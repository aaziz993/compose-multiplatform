package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.kotlin.model.configure

@Serializable
internal data class KotlinNativeCompilation(
    override val compilationName: String,
    override val defaultSourceSet: plugin.project.kotlin.kmp.model.KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: plugin.project.kotlin.model.KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = ProjectDependencyTransformingSerializer::class) ProjectDependency>? = null,
    // Interop DSL.

    val cinterops: List<DefaultCInteropSettings>? = null
) : plugin.project.kotlin.model.KotlinCompilation {

    context(Project)
    override fun applyTo(compilations: NamedDomainObjectContainer<out KotlinCompilation<*>>) {
        super.applyTo(compilations)

        compilations.configure {
            this as KotlinNativeCompilation

            this@KotlinNativeCompilation.cinterops?.forEach { cinterops ->
                cinterops {
                    cinterops.applyTo(this)
                }
            }
        }
    }
}
