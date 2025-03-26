package gradle.plugins.kmp.web

import gradle.api.trySet
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KotlinJsIrCompilation(
    override val compilationName: String, override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    override val binaries: @Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class) KotlinJsBinaryContainer? = null,
    override val packageJson: PackageJson? = null,
) : KotlinJsCompilation<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation) {
        super.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)
        packageJson?.let { packageJson ->
            receiver.packageJson {
                packageJson.applyTo(this)
            }
        }
    }
}

internal object KotlinJsIrCompilationTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinJsIrCompilation>(
        KotlinJsIrCompilation.serializer(),
    )
