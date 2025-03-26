package gradle.plugins.kmp.nat.apple.ios

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.api.applyTo
import gradle.plugins.kmp.nat.KotlinNativeCompilationKeyTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeSimulatorTestRun
import gradle.plugins.kmp.nat.KotlinNativeSimulatorTestRunTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeTargetWithSimulatorTests
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("iosSimulatorArm64")
internal data class KotlinIosSimulatorArm64Target(
    override val name: String = "iosSimulatorArm64",
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeSimulatorTestRunKeyTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinIosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinNativeTarget>()) { name, action ->
            project.kotlin.iosSimulatorArm64(name, action::execute)
        }
}
