package plugin.project.kmp.model.nat.apple.tvos

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
