package gradle.plugins.kotlin.targets.web

import gradle.accessors.kotlin
import gradle.accessors.moduleName
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.api.trySet
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.mpp.HasBinaries
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl

internal interface KotlinJsTargetDsl<T : org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>
    : KotlinTarget<T>,
    KotlinTargetWithNodeJsDsl,
    HasBinaries<@Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class) KotlinJsBinaryContainer>,
    HasConfigurableKotlinCompilerOptions<T, org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions> {

    abstract override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>?

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

        useCommonJs?.takeIfTrue()?.act(receiver::useCommonJs)
        useEsModules?.takeIfTrue()?.act(receiver::useEsModules)
        passAsArgumentToMainFunction?.let(receiver::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIfTrue()?.let { receiver.generateTypeScriptDefinitions() }
        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("jsCommon")
internal data class KotlinJsTargetDslImpl(
    override val name: String? = null,
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class) KotlinJsBinaryContainer? = null,
) : KotlinJsTargetDsl<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl> {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>()) { _, _ -> }
}
