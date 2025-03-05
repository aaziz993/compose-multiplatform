package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.gradle.model.HasBinaries

@Serializable
@SerialName("wasmWasi")
internal data class KotlinWasmWasiTarget(
    override val targetName: String = "wasmWasi",
    override val compilations: List<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinTarget, KotlinTargetWithNodeJsDsl, HasBinaries<KotlinJsBinaryContainer> {

    context(Project)
    override fun applyTo() {
        val target = create(kotlin::wasmWasi)

        super<KotlinTarget>.applyTo()

        (target as KotlinWasmWasiTargetDsl?) ?: return

        super<KotlinTargetWithNodeJsDsl>.applyTo(target)

        binaries.applyTo(target.binaries)
    }
}
