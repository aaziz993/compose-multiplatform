package gradle.plugins.kmp.web


import gradle.api.trySet
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named

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
    override fun applyTo(receiver: T) {
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
