package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("js")
internal data class KotlinJsTarget(
    override val targetName: String = "js",
    override val compilations: Set<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
) : KotlinJsTargetDsl<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl> {

    context(Project)
    override fun applyTo() =
        applyTo(
                project.kotlin.targets.matching { target ->
                    target::class == org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl::class
                },
                kotlin::js,
        )
}
