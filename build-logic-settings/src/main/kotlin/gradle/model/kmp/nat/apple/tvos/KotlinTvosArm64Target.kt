package gradle.model.kmp.nat.apple.tvos

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
@SerialName("tvosArm64")
internal data class KotlinTvosArm64Target(
    override val targetName: String = "tvosArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget(), KotlinTvosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::tvosArm64)

        super.applyTo()
    }
}
