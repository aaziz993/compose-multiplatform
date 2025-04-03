package gradle.plugins.kotlin.targets.web

import gradle.api.NamedObjectTransformingSerializer
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinSourceSet
import gradle.api.artifacts.Dependency
import gradle.plugins.kotlin.targets.web.tasks.Kotlin2JsCompileImpl
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@KeepGeneratedSerializer
@Serializable(with = KotlinJsIrCompilationObjectTransformingSerializer::class)
internal data class KotlinJsIrCompilation(
    override val name: String, override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val compileTaskProvider: Kotlin2JsCompileImpl? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: Set<Dependency>? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
    override val packageJsonHandlers: List<PackageJson>? = null,
) : KotlinJsCompilation<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation>()

private object KotlinJsIrCompilationObjectTransformingSerializer :
    NamedObjectTransformingSerializer<KotlinJsIrCompilation>(
        KotlinJsIrCompilation.generatedSerializer(),
    )
