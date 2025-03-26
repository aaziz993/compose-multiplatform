package gradle.plugins.kmp.nat

import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal abstract class KotlinNativeTargetWithSimulatorTests : KotlinNativeTargetWithTests<KotlinNativeSimulatorTestRun>()

@Serializable
@SerialName("nativeWithSimulatorTests")
internal data class KotlinNativeTargetWithHostTestsImpl(
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeSimulatorTestRunKeyTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests() {

    override val name: String
        get() = ""
}
