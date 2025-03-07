package gradle.model.kotlin.kmp.nat.android

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kotlin.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilation
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilerOptions

@Serializable
@SerialName("androidNativeArm32")
internal data class KotlinAndroidNativeArm32(
    override val targetName: String = "androidNativeArm32",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::androidNativeArm32)

        super.applyTo()
    }
}
