package gradle.plugins.kmp.web

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kotlin.KotlinCompilation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KotlinWasmTargetDsl<T : org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl>
    : KotlinTarget<T>, HasBinaries<KotlinJsBinaryContainer> {

    override val compilations: Set<KotlinJsIrCompilation>?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("wasm")
internal data class KotlinWasmTargetDslImpl(
    override val targetName: String = "",
    override val compilations: Set<KotlinJsIrCompilation>? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinWasmTargetDsl<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl> {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl>()) { _, _ -> }
}
