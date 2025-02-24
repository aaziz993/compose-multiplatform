package plugin.project.kotlin.model.language.web

import gradle.moduleName
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.kotlin.model.language.HasBinaries
import plugin.project.kotlin.model.language.HasConfigurableKotlinCompilerOptions
import plugin.project.kotlin.model.language.KotlinTarget

internal interface KotlinJsTargetDsl : KotlinTarget, KotlinTargetWithNodeJsDsl, HasBinaries<KotlinJsBinaryContainer>, HasConfigurableKotlinCompilerOptions<KotlinJsCompilerOptions> {

    val moduleName: String?

    val browser: KotlinJsBrowserDsl?

    val useCommonJs: Boolean?

    val useEsModules: Boolean?

    /**
     * The function accepts [jsExpression] and puts this expression as the "args: Array<String>" argument in place of main-function call
     */
    val passAsArgumentToMainFunction: String?

    val generateTypeScriptDefinitions: Boolean?

    context(Project)
    @OptIn(ExperimentalMainFunctionArgumentsDsl::class)
    fun applyTo(target: KotlinJsTargetDsl) {
        super.applyTo(target)

        target.moduleName = moduleName ?: moduleName

        nodejs?.let { nodejs ->
            target.nodejs {
                nodejs.applyTo(this)
            }
        }

        browser?.let { browser ->
            target.browser {
                browser.applyTo(this)
            }
        }

        useCommonJs?.takeIf { it }?.run { target.useCommonJs() }
        useEsModules?.takeIf { it }?.run { target.useEsModules() }
        passAsArgumentToMainFunction?.let(target::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { target.generateTypeScriptDefinitions() }

        binaries?.let { binaries ->
            binaries.library?.takeIf { it }?.run { target.binaries.library() }
            binaries.executable?.takeIf { it }?.run { target.binaries.executable() }
        }
    }
}
