package gradle.plugins.kotlin.targets.web

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.mpp.HasBinaries
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal interface KotlinWasmTargetDsl<T : org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl>
    : KotlinTarget<T>,
    HasBinaries<@Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class) KotlinJsBinaryContainer> {

    override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>?

    context(Project)
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("wasm")
internal data class KotlinWasmTargetDslImpl(
    override val name: String = "",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinWasmTargetDsl<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl> {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl>()) { _, _ -> }
}
