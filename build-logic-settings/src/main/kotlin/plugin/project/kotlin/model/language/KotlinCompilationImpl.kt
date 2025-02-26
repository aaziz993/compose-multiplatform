package plugin.project.kotlin.model.language

import kotlinx.serialization.Serializable
import plugin.model.dependency.ProjectDependency

@Serializable
internal data class KotlinCompilationImpl(
    override val compilationName: String, override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<ProjectDependency>? = null,
) : KotlinCompilation
