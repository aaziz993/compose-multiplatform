package plugin.project.kmp.model.nat.apple.macos

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kmp.model.nat.KotlinNativeCompilationTransformingSerializer
import plugin.project.kmp.model.nat.KotlinNativeCompilerOptions
import plugin.project.kmp.model.nat.KotlinNativeHostTestRun
import plugin.project.kmp.model.nat.KotlinNativeTargetWithHostTests

@Serializable
@SerialName("macosArm64")
internal data class KotlinMacosArm64Target(
    override val targetName: String = "macosArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests(), KotlinMacosTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::macosArm64)

        super.applyTo()
    }
}
