package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

@Serializable
@SerialName("js")
internal data class KotlinJsTarget(
    override val targetName: String = "js",
    override val compilations: List<@Serializable(with = KotlinJsCompilationTransformingSerializer::class) KotlinJsCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinJsTargetDsl() {

    context(Project)
    override fun applyTo() = super.applyTo(kotlin.js(targetName) as KotlinTarget)
}
