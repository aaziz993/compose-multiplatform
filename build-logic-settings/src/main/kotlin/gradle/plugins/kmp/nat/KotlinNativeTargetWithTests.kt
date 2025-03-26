package gradle.plugins.kmp.nat

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kmp.KotlinTargetTestRun
import gradle.plugins.kmp.KotlinTargetWithTests
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTargetWithTests<T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>> :
    KotlinNativeTarget<T>(), KotlinTargetWithTests<T> {

    override val testRuns: LinkedHashSet<KotlinNativeBinaryTestRun<out org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>>

    context(Project)
    override fun applyTo(receiver: T) {
        super<KotlinNativeTarget>.applyTo(receiver)
        super<KotlinTargetWithTests>.applyTo(receiver)
    }
}

@Serializable
@SerialName("nativeWithTests")
internal data class KotlinNativeTargetWithTestsImpl(
    override val name: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<@Serializable(with = KotlinNativeBinaryTestRunKeyTransformingSerializer::class) KotlinNativeBinaryTestRunImpl>? = null,
) : KotlinNativeTargetWithTests<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>()) { _, _ -> }
}


