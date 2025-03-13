package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

@Serializable
@SerialName("wasmJs")
internal data class KotlinWasmJsTarget(
    override val targetName: String = "wasmJs",
    override val compilations: List<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
    val d8: Boolean? = null,
    val d8Dsl: KotlinWasmD8Dsl? = null,
) : KotlinWasmTargetDsl, KotlinJsTargetDsl {

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinWasmTargetDsl>.applyTo(named)
        super<KotlinJsTargetDsl>.applyTo(named)

        named as KotlinWasmJsTargetDsl

        d8?.takeIf { it }?.run { named.d8() }

        d8Dsl?.let { d8Dsl ->
            named.d8 {
                d8Dsl.applyTo(this, "$moduleName-${named.targetName}")
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(kotlin.targets.withType<KotlinWasmJsTargetDsl>(), kotlin::wasmJs)
}
