package gradle.plugins.kmp.web

import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.project.Dependency
import gradle.plugins.project.DependencyTransformingSerializer
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

@Serializable
internal data class KotlinJsIrCompilation(
    override val compilationName: String, override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: List<String>? = null,
    override val output: KotlinCompilationOutput? = null,
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
