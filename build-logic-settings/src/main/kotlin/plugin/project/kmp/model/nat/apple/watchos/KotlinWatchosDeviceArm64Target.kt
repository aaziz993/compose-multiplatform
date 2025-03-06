package plugin.project.kmp.model.nat.apple.watchos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kmp.model.nat.KotlinNativeCompilationTransformingSerializer
import plugin.project.kmp.model.nat.KotlinNativeCompilerOptions
import plugin.project.kmp.model.nat.KotlinNativeTarget

@Serializable
@SerialName("watchosDeviceArm64")
internal data class KotlinWatchosDeviceArm64Target(
    override val targetName: String = "watchosDeviceArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget(), KotlinWatchosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::watchosDeviceArm64)

        super.applyTo()
    }
}
