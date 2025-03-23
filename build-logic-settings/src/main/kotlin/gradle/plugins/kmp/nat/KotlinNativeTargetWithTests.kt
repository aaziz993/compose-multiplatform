package gradle.plugins.kmp.nat

import gradle.accessors.kotlin

import gradle.plugins.kmp.KotlinTargetWithTests
import gradle.plugins.kmp.nat.KotlinNativeTarget
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTargetWithTests<T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests> :
    KotlinNativeTarget(), KotlinTargetWithTests<T> {

        context(Project)
    override fun applyTo(receiver: T) {
        super<KotlinNativeTarget>.applyTo(named)
        super<KotlinTargetWithTests>.applyTo(named)
    }

}

@Serializable
@SerialName("nativeWithTests")
internal data class KotlinNativeTargetWithTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeBinaryTestRunTransformingSerializer::class) KotlinNativeBinaryTestRunImpl>? = null,
) : KotlinNativeTargetWithTests<KotlinNativeBinaryTestRun>() {

    override val targetName: String
        get() = ""

    context(Project)
    override fun applyTo() =
        super<KotlinNativeTarget>.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>())

}


