package gradle.plugins.kotlin.targets.nat

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.KotlinTargetWithTests
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.NativeBinaryTestRunSource

internal abstract class KotlinNativeTargetWithTests<
    T : org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<R>,
    R : org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun
    > : KotlinNativeTarget<T>(), KotlinTargetWithTests<T, NativeBinaryTestRunSource, R> {

    abstract override val testRuns: LinkedHashSet<out KotlinNativeBinaryTestRun<R>>?

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
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<@Serializable(with = KotlinNativeBinaryTestRunKeyTransformingSerializer::class) KotlinNativeBinaryTestRunImpl>? = null,
) : KotlinNativeTargetWithTests<
    org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>,
    org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun,
    >() {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>()) { _, _ -> }
}


