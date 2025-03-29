package gradle.plugins.kotlin.targets.web

import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinCompilationTransformingSerializer
import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.project.Dependency
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer

import kotlinx.serialization.Serializable
import org.gradle.api.Project

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = KotlinJsIrCompilationKeyValueTransformingSerializer::class)
internal data class KotlinJsIrCompilation(
    override val name: String, override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val compileTaskProvider: Kotlin2JsCompileImpl? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: Set<Dependency>? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
    override val packageJson: PackageJson? = null,
) : KotlinJsCompilation<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation) {
        super.applyTo(receiver)

        packageJson?.let { packageJson ->
            receiver.packageJson {
                packageJson.applyTo(this)
            }
        }
    }
}

private object KotlinJsIrCompilationKeyValueTransformingSerializer :
    KotlinCompilationTransformingSerializer<KotlinJsIrCompilation>(
        KotlinJsIrCompilation.generatedSerializer(),
    )
