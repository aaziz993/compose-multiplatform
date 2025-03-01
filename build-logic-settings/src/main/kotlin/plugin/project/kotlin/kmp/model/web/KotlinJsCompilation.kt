package plugin.project.kotlin.kmp.model.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.kotlin.model.HasBinaries
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.configure

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
) : KotlinCompilation, HasBinaries<KotlinJsBinaryContainer> {

    context(Project)
    override fun applyTo(compilations: NamedDomainObjectContainer<out org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>) {
        super.applyTo(compilations)

        compilations.configure {
            this as KotlinJsCompilation

            this@KotlinJsCompilation.binaries.applyTo(binaries)
            ::outputModuleName trySet this@KotlinJsCompilation.outputModuleName
            this@KotlinJsCompilation.packageJson?.let { packageJson ->
                packageJson {
                    packageJson.applyTo(this)
                }
            }
        }
    }
}
