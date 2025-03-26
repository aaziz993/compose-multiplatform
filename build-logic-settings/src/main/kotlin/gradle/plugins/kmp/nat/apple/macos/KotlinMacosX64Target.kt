package gradle.plugins.kmp.nat.apple.macos

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.api.applyTo
import gradle.plugins.kmp.nat.KotlinNativeCompilationKeyTransformingSerializer
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
@SerialName("macosX64")
internal data class KotlinMacosX64Target(
    override val name: String = "macosX64",
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<@Serializable(with = KotlinNativeHostTestRunKeyTransformingSerializer::class) KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests(), KotlinMacosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinNativeTarget>()) { name, action ->
            project.kotlin.macosX64(name, action::execute)
        }
}
