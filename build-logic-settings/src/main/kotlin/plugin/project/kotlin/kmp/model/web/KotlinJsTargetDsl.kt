package plugin.project.kotlin.kmp.model.web

import gradle.moduleName
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.configure

@Serializable
internal abstract class KotlinJsTargetDsl : KotlinTarget, KotlinTargetWithNodeJsDsl,
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
    override fun applyTo(targets: NamedDomainObjectCollection<out org.jetbrains.kotlin.gradle.plugin.KotlinTarget>) {
        super<KotlinTarget>.applyTo(targets)

        targets.configure {
            this as org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

            this@KotlinJsTargetDsl.compilerOptions?.applyTo(compilerOptions)

            moduleName = this@KotlinJsTargetDsl.moduleName ?: project.moduleName

            super<KotlinTargetWithNodeJsDsl>.applyTo(this)

            this@KotlinJsTargetDsl.browser?.let { browser ->
                browser {
                    browser.applyTo(this, "$moduleName-${targetName}.js")
                }
            }

            this@KotlinJsTargetDsl.useCommonJs?.takeIf { it }?.run { useCommonJs() }
            this@KotlinJsTargetDsl.useEsModules?.takeIf { it }?.run { useEsModules() }
            this@KotlinJsTargetDsl.passAsArgumentToMainFunction?.let(::passAsArgumentToMainFunction)
            this@KotlinJsTargetDsl.generateTypeScriptDefinitions?.takeIf { it }?.let { generateTypeScriptDefinitions() }
            this@KotlinJsTargetDsl.binaries.applyTo(binaries)
        }
    }
}
