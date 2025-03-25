package gradle.plugins.kmp.web

import gradle.accessors.moduleName
import gradle.api.trySet
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl

internal interface KotlinJsTargetDsl<T : org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>
    : KotlinTarget<T>,
    KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer>,
    HasConfigurableKotlinCompilerOptions<T, org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions> {

    abstract override val compilations: Set<KotlinJsIrCompilation>?

    val moduleName: String?

    val browser: KotlinJsBrowserDsl?

    val useCommonJs: Boolean?

    val useEsModules: Boolean?

    /**
     * The function accepts [jsExpression] and puts this expression as the "args: Array<String>" argument in place of main-function call
     */
    val passAsArgumentToMainFunction: String?

    val generateTypeScriptDefinitions: Boolean?

    context(project: Project)
    @OptIn(ExperimentalMainFunctionArgumentsDsl::class)
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)

        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        receiver::moduleName trySet (this@KotlinJsTargetDsl.moduleName
            ?: targetName
                ?.takeIf(String::isNotEmpty)
                ?.let { targetName -> "${project.moduleName}-$targetName" })

        super<KotlinTargetWithNodeJsDsl>.applyTo(receiver, receiver.moduleName!!)

        browser?.let { browser ->
            receiver.browser {
                browser.applyTo(this, receiver.moduleName!!)
            }
        }

        useCommonJs?.takeIf { it }?.run { receiver.useCommonJs() }
        useEsModules?.takeIf { it }?.run { receiver.useEsModules() }
        passAsArgumentToMainFunction?.let(receiver::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { receiver.generateTypeScriptDefinitions() }
        binaries?.applyTo(receiver.binaries)
    }
}
