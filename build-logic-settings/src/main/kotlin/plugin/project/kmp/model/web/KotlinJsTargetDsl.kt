package plugin.project.kmp.model.web

import gradle.kotlin
import gradle.moduleName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import plugin.project.gradle.model.HasBinaries
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions

internal interface KotlinJsTargetDsl : KotlinTarget, KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer>, HasConfigurableKotlinCompilerOptions<KotlinJsCompilerOptions> {

    abstract override val compilations: List<KotlinJsCompilation>?

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
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

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
        super<KotlinTarget>.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>())
}

@Serializable
@SerialName("jsAndWasmJs")
internal data class KotlinJsTargetDslImpl(
    override val compilations: List<KotlinJsCompilation>? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
    override val compilerOptions: KotlinJsCompilerOptions? = null,
) : KotlinJsTargetDsl {

    override val targetName: String
        get() = ""
}
