package gradle.plugins.kmp.web

import gradle.accessors.kotlin

import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kotlin.KotlinCompilation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KotlinWasmTargetDsl : KotlinTarget, HasBinaries<KotlinJsBinaryContainer> {

        context(Project)
    override fun applyTo(named: T) {
        super<KotlinTarget>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl

        binaries.applyTo(named.binaries)
    }

    context(Project)
    override fun applyTo() =
        super<KotlinTarget>.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl>(), kotlin::wasmWasi)
}

@Serializable
@SerialName("wasm")
internal data class KotlinWasmTargetDslImpl(
    override val targetName: String = "",
    override val compilations: List<KotlinCompilation>? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinWasmTargetDsl
