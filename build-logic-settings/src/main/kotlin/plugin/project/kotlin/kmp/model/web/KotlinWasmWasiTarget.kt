package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasBinaries
import plugin.project.kotlin.model.configure

@Serializable
@SerialName("wasmWasi")
internal data class KotlinWasmWasiTarget(
    override val targetName: String = "",
    override val compilations: List<KotlinJsCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinTarget, KotlinTargetWithNodeJsDsl, HasBinaries<KotlinJsBinaryContainer> {

    context(Project)
    override fun applyTo() {
        val targets =
            if (targetName.isEmpty())
                kotlin.targets.withType<KotlinNativeTarget>()
            else container { kotlin.androidNativeArm32(targetName) }

        super<KotlinTarget>.applyTo(targets)

        targets.configure {
            this as KotlinWasmWasiTargetDsl

            super<KotlinTargetWithNodeJsDsl>.applyTo(this)

//            this@KotlinWasmWasiTarget.binaries.applyTo(binaries)
        }
    }
}
