package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import gradle.accessors.moduleName
import gradle.api.applyTo
import gradle.plugins.kmp.HasBinaries
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl

@Serializable
@SerialName("wasmWasi")
internal data class KotlinWasmWasiTargetDsl(
    override val name: String = "wasmWasi",
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
) : KotlinWasmTargetDsl<KotlinWasmWasiTargetDsl>,
    KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer> {

    context(Project)
    override fun applyTo(receiver: KotlinWasmWasiTargetDsl) {
        super<KotlinWasmTargetDsl>.applyTo(receiver)
        super<KotlinTargetWithNodeJsDsl>.applyTo(receiver, "${project.moduleName}-${receiver.targetName}")
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinWasmWasiTargetDsl>()) { name, action ->
            kotlin.wasmWasi(name, action::execute)
        }
}
