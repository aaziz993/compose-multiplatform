package plugin.project.kotlin.kmp.model.web

import gradle.moduleName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import plugin.project.kotlin.kmp.model.KotlinTarget

@Serializable
internal sealed class KotlinJsTargetDsl : KotlinTarget, KotlinTargetWithNodeJsDsl,
    plugin.project.kotlin.kmp.model.nat.HasBinaries<KotlinJsBinaryContainer>, plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions<plugin.project.kotlin.kmp.model.web.KotlinJsCompilerOptions> {

    abstract override val compilations: List<KotlinJsCompilation>?

    abstract val moduleName: String?

    abstract val browser: KotlinJsBrowserDsl?

    abstract val useCommonJs: Boolean?

    abstract val useEsModules: Boolean?

    /**
     * The function accepts [jsExpression] and puts this expression as the "args: Array<String>" argument in place of main-function call
     */
    abstract val passAsArgumentToMainFunction: String?

    abstract val generateTypeScriptDefinitions: Boolean?

    context(Project)
    @OptIn(ExperimentalMainFunctionArgumentsDsl::class)
    protected fun applyTo(target: org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl) {
        super<KotlinTarget>.applyTo(target)

        compilerOptions?.applyTo(target.compilerOptions)

        target.moduleName = moduleName ?: project.moduleName

        super<KotlinTargetWithNodeJsDsl>.applyTo(target)

        browser?.let { browser ->
            target.browser {
                browser.applyTo(this, "$moduleName-${targetName}.js")
            }
        }

        useCommonJs?.takeIf { it }?.run { target.useCommonJs() }
        useEsModules?.takeIf { it }?.run { target.useEsModules() }
        passAsArgumentToMainFunction?.let(target::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { target.generateTypeScriptDefinitions() }

        binaries.applyTo(target.binaries)
    }
}
