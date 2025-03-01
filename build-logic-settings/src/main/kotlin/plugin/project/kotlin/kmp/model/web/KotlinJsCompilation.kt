package plugin.project.kotlin.kmp.model.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer

@Serializable
internal data class KotlinJsCompilation(
    override val compilationName: String, override val defaultSourceSet: plugin.project.kotlin.kmp.model.KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: plugin.project.kotlin.model.KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = ProjectDependencyTransformingSerializer::class) ProjectDependency>? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
    var outputModuleName: String? = null,
    val packageJson: PackageJson? = null,
) : plugin.project.kotlin.model.KotlinCompilation, plugin.project.kotlin.model.HasBinaries<KotlinJsBinaryContainer> {

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
