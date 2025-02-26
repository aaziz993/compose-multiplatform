package plugin.project.kotlin.model.language.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import plugin.model.dependency.ProjectDependency
import plugin.project.kotlin.model.language.HasBinaries
import plugin.project.kotlin.model.language.KotlinCompilation
import plugin.project.kotlin.model.language.KotlinCompilationOutput
import plugin.project.kotlin.model.language.KotlinSourceSet

@Serializable
internal data class KotlinJsCompilation(
    override val compilationName: String, override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<ProjectDependency>? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
    var outputModuleName: String? = null,
    val packageJson: PackageJson? = null,
) : KotlinCompilation, HasBinaries<KotlinJsBinaryContainer> {

    context(Project)
    fun applyTo(compilation: KotlinJsCompilation) {
        super.applyTo(compilation)

        binaries.applyTo(compilation.binaries)
        compilation::outputModuleName trySet outputModuleName
        packageJson?.let { packageJson ->
            compilation.packageJson {
                packageJson.applyTo(this)
            }
        }
    }
}
