package gradle.plugins.kmp.nat

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import gradle.plugins.kotlin.KotlinCompilation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary

internal abstract class KotlinNativeTarget<T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>
    : KotlinTarget<T>,
    HasConfigurableKotlinCompilerOptions<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget, org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions>,
    HasBinaries<@Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer> {

    abstract override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>?

    context(Project)
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("native")
internal data class KotlinNativeTargetImpl(
    override val name: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget() {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()) { _, _ -> }
}
