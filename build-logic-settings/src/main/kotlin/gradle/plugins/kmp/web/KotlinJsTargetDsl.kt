package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import gradle.accessors.moduleName

import gradle.api.trySet
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

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
    override fun applyTo(receiver: T) {
        super<KotlinTarget>._applyTo(named)

        named as org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named)

        named::moduleName trySet (this@KotlinJsTargetDsl.moduleName
            ?: targetName
                .takeIf(String::isNotEmpty)
                ?.let { targetName -> "${project.moduleName}-$targetName" })

        super<KotlinTargetWithNodeJsDsl>.applyTo(named, named.moduleName!!)

        browser?.let { browser ->
            named.browser {
                browser.applyTo(this, named.moduleName!!)
            }
        }

        useCommonJs?.takeIf { it }?.run { named.useCommonJs() }
        useEsModules?.takeIf { it }?.run { named.useEsModules() }
        passAsArgumentToMainFunction?.let(named::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { named.generateTypeScriptDefinitions() }
        binaries.applyTo(named.binaries)
    }

    context(Project)
    override fun applyTo() = with(project) {
        super<KotlinTarget>.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>())
    }
}

@Serializable
@SerialName("jsCommon")
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
