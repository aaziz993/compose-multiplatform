package gradle.plugins.kmp.nat

import org.gradle.kotlin.dsl.withType
import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class KotlinNativeTargetWithHostTests
    : KotlinNativeTargetWithTests<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests>()

@Serializable
@SerialName("nativeWithHostTests")
internal data class KotlinNativeTargetWithSimulatorTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<@Serializable(with = KotlinNativeHostTestRunKeyTransformingSerializer::class) KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests() {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests>()) { _, _ -> }
}
