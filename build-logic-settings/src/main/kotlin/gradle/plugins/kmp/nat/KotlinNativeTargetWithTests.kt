package gradle.plugins.kmp.nat

import gradle.api.applyTo
import gradle.plugins.kmp.KotlinTargetWithTests
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTargetWithTests<T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>> :
    KotlinNativeTarget<T>(), KotlinTargetWithTests<T> {

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<KotlinNativeTarget>.applyTo(receiver)
        super<KotlinTargetWithTests>.applyTo(receiver)
    }
}

@Serializable
@SerialName("nativeWithTests")
internal data class KotlinNativeTargetWithTestsImpl(
    override val compilations: Set<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: Set<@Serializable(with = KotlinNativeBinaryTestRunTransformingSerializer::class) KotlinNativeBinaryTestRunImpl>? = null,
) : KotlinNativeTargetWithTests<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>()) { _, _ -> }
}


