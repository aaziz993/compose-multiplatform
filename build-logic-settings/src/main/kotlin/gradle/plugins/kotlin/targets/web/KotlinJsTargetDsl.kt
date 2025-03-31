package gradle.plugins.kotlin.targets.web

import gradle.accessors.kotlin
import gradle.accessors.moduleName
import gradle.api.applyTo
import gradle.api.provider.tryAssign
import gradle.api.publish.maven.MavenPublication
import gradle.reflect.trySet
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.mpp.HasBinaries
import gradle.reflect.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl

internal interface KotlinJsTargetDsl<T : org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>
    : KotlinTarget<T>,
    KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer>,
    HasConfigurableKotlinCompilerOptions<T, org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions> {

    abstract override val compilations: LinkedHashSet<KotlinJsIrCompilation>?

    val outputModuleName: String?

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
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)

        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        receiver.outputModuleName tryAssign (this@KotlinJsTargetDsl.outputModuleName
            ?: targetName
                ?.takeIf(String::isNotEmpty)
                ?.let { targetName -> "${project.moduleName}-$targetName" })

        super<KotlinTargetWithNodeJsDsl>.applyTo(receiver, receiver.moduleName!!)

        browser?.let { browser ->
            receiver.browser {
                browser.applyTo(this, receiver.outputModuleName.get())
            }
        }

        receiver::useCommonJs trySet useCommonJs
        receiver::useEsModules trySet useEsModules
        receiver::passAsArgumentToMainFunction trySet passAsArgumentToMainFunction
        receiver::generateTypeScriptDefinitions trySet generateTypeScriptDefinitions
        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("jsCommon")
internal data class KotlinJsTargetDslImpl(
    override val name: String? = null,
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val outputModuleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
) : KotlinJsTargetDsl<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl> {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>()) { _, _ -> }
}
