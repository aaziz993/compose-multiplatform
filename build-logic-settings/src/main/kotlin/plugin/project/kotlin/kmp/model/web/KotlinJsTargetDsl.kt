package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import gradle.moduleName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.gradle.model.HasBinaries
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions
import org.gradle.kotlin.dsl.withType

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
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        named as KotlinJsTargetDsl

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named)

        named.moduleName = moduleName ?: project.moduleName

        super<KotlinTargetWithNodeJsDsl>.applyTo(named)

        browser?.let { browser ->
            named.browser {
                browser.applyTo(this, "$moduleName-${targetName}.js")
            }
        }

        useCommonJs?.takeIf { it }?.run { named.useCommonJs() }
        useEsModules?.takeIf { it }?.run { named.useEsModules() }
        passAsArgumentToMainFunction?.let(named::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { named.generateTypeScriptDefinitions() }
        binaries.applyTo(named.binaries)
    }

    context(Project)
    override fun applyTo() =
        super<KotlinTarget>.applyTo(kotlin.targets.withType<KotlinJsTargetDsl>())
}
