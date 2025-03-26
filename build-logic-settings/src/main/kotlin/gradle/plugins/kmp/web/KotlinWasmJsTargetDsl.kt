package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import gradle.api.applyTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

@Serializable
@SerialName("wasmJs")
internal data class KotlinWasmJsTargetDsl(
    override val name: String = "wasmJs",
    override val compilations: Set<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
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

        d8?.takeIf { it }?.run { receiver.d8() }

        d8Dsl?.let { d8Dsl ->
            receiver.d8 {
                d8Dsl.applyTo(this, "$moduleName-${receiver.targetName}")
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinWasmJsTargetDsl>(), kotlin::wasmJs)
}
