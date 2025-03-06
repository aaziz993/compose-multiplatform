package plugin.project.kotlin.kmp.model.nat.android

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilationTransformingSerializer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilerOptions

@Serializable
@SerialName("androidNativeArm32")
internal data class KotlinAndroidNativeArm32(
    override val targetName: String = "androidNativeArm32",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    context(Project)
    override fun applyTo() {
        create(kotlin::androidNativeArm32)

        super.applyTo()
    }
}
