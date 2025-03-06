package plugin.project.kmp.model.web

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import plugin.project.gradle.model.HasBinaries
import plugin.project.kmp.model.KotlinTarget

@Serializable
@SerialName("wasmWasi")
internal data class KotlinWasmWasiTarget(
    override val targetName: String = "wasmWasi",
    override val compilations: List<@Serializable(with = KotlinJsIrCompilationTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinTarget, KotlinTargetWithNodeJsDsl, HasBinaries<KotlinJsBinaryContainer> {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        named as KotlinWasmWasiTargetDsl

        super<KotlinTargetWithNodeJsDsl>.applyTo(named)

        binaries.applyTo(named.binaries)
    }

    context(Project)
    override fun applyTo() {
        create(kotlin::wasmWasi)

        super<KotlinTarget>.applyTo(kotlin.targets.withType<KotlinWasmWasiTargetDsl>())
    }
}
