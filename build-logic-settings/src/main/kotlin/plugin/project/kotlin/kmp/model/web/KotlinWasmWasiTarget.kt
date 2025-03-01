package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.KotlinTarget

@Serializable
@SerialName("wasmWasi")
internal data class KotlinWasmWasiTarget(
    override val targetName: String = "",
    override val compilations: List<KotlinJsCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinTarget, KotlinTargetWithNodeJsDsl, plugin.project.kotlin.kmp.model.nat.HasBinaries<KotlinJsBinaryContainer> {

    context(Project)
    override fun applyTo() {
        val target = targetName.takeIf(String::isNotEmpty)?.let(kotlin::wasmWasi) ?: kotlin.wasmWasi()

        super<KotlinTarget>.applyTo(target)

        super<KotlinTargetWithNodeJsDsl>.applyTo(target)
    }
}
