package plugin.project.kotlin.kmp.model.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import plugin.project.model.Dependency
import plugin.project.model.DependencyTransformingSerializer
import plugin.project.kotlin.model.KotlinCompilationTransformingSerializer

@Serializable
internal data class KotlinJsIrCompilation(
    override val compilationName: String, override val defaultSourceSet: plugin.project.kotlin.kmp.model.KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: plugin.project.kotlin.model.KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
    override val outputModuleName: String? = null,
    override val packageJson: PackageJson? = null,
) : KotlinJsCompilation {

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

internal object KotlinJsIrCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinJsIrCompilation>(
        KotlinJsIrCompilation.serializer(),
    )
