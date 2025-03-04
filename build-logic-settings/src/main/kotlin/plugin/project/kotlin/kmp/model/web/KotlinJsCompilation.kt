package plugin.project.kotlin.kmp.model.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.ProjectDependencyTransformingSerializer
import plugin.project.kotlin.model.HasBinaries
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.KotlinCompilationTransformingSerializer

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
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

        binaries.applyTo(named.binaries)
        named::outputModuleName trySet outputModuleName

        packageJson?.let { packageJson ->
            named.packageJson {
                packageJson.applyTo(this)
            }
        }
    }
}

internal object KotlinJsCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinJsCompilation>(
        KotlinJsCompilation.serializer(),
    )
