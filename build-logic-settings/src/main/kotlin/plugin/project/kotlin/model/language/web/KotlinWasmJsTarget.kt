package plugin.project.kotlin.model.language.web

import gradle.kotlin
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsNodeDsl

@Serializable
internal data class KotlinWasmJsTarget(
    override val targetName: String,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions?
) : KotlinJsTargetDsl {

    context(Project)
    fun applyTo() {
        targetName.takeIf(String::isNotEmpty)?.also { targetName ->
            kotlin.wasmJs(targetName) {
                super.applyTo(this)
            }
        } ?: kotlin.wasmJs {
            super.applyTo(this)
        }
    }
}
