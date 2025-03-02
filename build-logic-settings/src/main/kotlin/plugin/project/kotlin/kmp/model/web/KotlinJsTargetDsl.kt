package plugin.project.kotlin.kmp.model.web

import gradle.moduleName
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasBinaries
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions

@Serializable
internal abstract class KotlinJsTargetDsl : KotlinTarget, KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer>, HasConfigurableKotlinCompilerOptions<KotlinJsCompilerOptions> {

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
    override fun applyTo(target: org.jetbrains.kotlin.gradle.plugin.KotlinTarget) {
        super<KotlinTarget>.applyTo(target)

        target as org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

        super<HasConfigurableKotlinCompilerOptions>.applyTo(target)

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
