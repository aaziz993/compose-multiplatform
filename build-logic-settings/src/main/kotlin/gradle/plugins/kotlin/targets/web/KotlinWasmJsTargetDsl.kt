package gradle.plugins.kotlin.targets.web

import gradle.accessors.kotlin
import gradle.accessors.moduleName
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.ifTrue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

@Serializable
@SerialName("wasmJs")
internal data class KotlinWasmJsTargetDsl(
    override val name: String = "wasmJs",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val outputModuleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
    val d8: Boolean? = null,
    val d8Dsl: KotlinWasmD8Dsl? = null,
) : KotlinWasmTargetDsl<KotlinWasmJsTargetDsl>,
    KotlinJsTargetDsl<KotlinWasmJsTargetDsl> {

    context(Project)
    override fun applyTo(receiver: KotlinWasmJsTargetDsl) {
        super<KotlinWasmTargetDsl>.applyTo(receiver)
        super<KotlinJsTargetDsl>.applyTo(receiver)

        d8?.ifTrue(receiver::d8)

        d8Dsl?.let { d8Dsl ->
            receiver.d8 {
                d8Dsl.applyTo(this, "${project.moduleName}-${receiver.targetName}")
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinWasmJsTargetDsl>()) { name, action ->
            kotlin.wasmJs(name, action::execute)
        }
}
