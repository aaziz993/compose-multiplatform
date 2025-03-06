package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("wasmJs")
internal data class KotlinWasmJsTarget(
    override val targetName: String = "wasmJs",
    override val compilations: List<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
    override val useD8: Boolean? = null,
    override val d8: KotlinWasmD8Dsl? = null,
) : KotlinWasmJsTargetDsl {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::wasmJs)

        super.applyTo()
    }
}
