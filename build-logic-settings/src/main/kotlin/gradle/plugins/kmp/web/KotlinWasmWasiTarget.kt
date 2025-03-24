package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import gradle.accessors.moduleName
import gradle.plugins.kmp.HasBinaries
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl

@Serializable
@SerialName("wasmWasi")
internal data class KotlinWasmWasiTarget(
    override val targetName: String = "wasmWasi",
    override val compilations: List<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinWasmTargetDsl,
    KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer> {

        context(project: Project)
    override fun applyTo(receiver: T) {
        super<KotlinWasmTargetDsl>.applyTo(named)

        named as KotlinWasmWasiTargetDsl

        super<KotlinTargetWithNodeJsDsl>.applyTo(named, "$moduleName-${named.targetName}")
    }

    context(project: Project)
    override fun applyTo() =
        super<KotlinWasmTargetDsl>.applyTo(kotlin.targets.withType<KotlinWasmWasiTargetDsl>(), kotlin::wasmWasi)
}
