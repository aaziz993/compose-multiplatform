package gradle.model.kotlin.kmp.nat.apple.watchos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kotlin.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilation
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilerOptions
import gradle.model.kotlin.kmp.nat.KotlinNativeTarget

@Serializable
@SerialName("watchosArm32")
internal data class KotlinWatchosArm32Target(
    override val targetName: String = "watchosArm32",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget(), KotlinWatchosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::watchosArm32)

        super.applyTo()
    }
}
