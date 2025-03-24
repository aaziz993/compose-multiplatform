package gradle.plugins.kmp.nat

import gradle.api.applyTo
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTarget<T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>
    : KotlinTarget<T>,
    HasConfigurableKotlinCompilerOptions<T, org.jetbrains.kotlin.gradle.dsl.KotlinNativeCompilerOptions>,
    HasBinaries<KotlinNativeBinaryContainer> {

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<KotlinTarget>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)
        super<HasConfigurableKotlinCompilerOptions>.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)
    }
}

@Serializable
@SerialName("native")
internal data class KotlinNativeTargetImpl(
    override val targetName: String? = null,
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()) { _, _ -> }
}
