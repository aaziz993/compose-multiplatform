package gradle.plugins.kmp.nat.apple.macos

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.plugins.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeHostTestRun
import gradle.plugins.kmp.nat.KotlinNativeHostTestRunTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeTargetWithHostTests
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("macosArm64")
internal data class KotlinMacosArm64Target(
    override val targetName: String = "macosArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeHostTestRunTransformingSerializer::class) KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests(), KotlinMacosTarget {

    context(Project)
    override fun applyTo() =
        super.applyTo(project.kotlin.targets.withType<KotlinNativeTarget>(), kotlin::macosArm64)
}
