package gradle.model.kmp.nat.apple.ios

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kmp.nat.KotlinNativeCompilation
import gradle.model.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kmp.nat.KotlinNativeCompilerOptions
import gradle.model.kmp.nat.KotlinNativeTarget

@Serializable
@SerialName("iosArm64")
internal data class IosArm64(
    override val targetName: String = "iosArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget(), KotlinIosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::iosArm64)

        super.applyTo()
    }
}
