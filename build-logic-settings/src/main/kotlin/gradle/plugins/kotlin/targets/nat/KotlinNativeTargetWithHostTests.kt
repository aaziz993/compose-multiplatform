package gradle.plugins.kotlin.targets.nat

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinNativeTargetWithHostTests
    : KotlinNativeTargetWithTests<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests, org.jetbrains.kotlin.gradle.targets.native.KotlinNativeHostTestRun>()

@Serializable
@SerialName("nativeWithHostTests")
internal data class KotlinNativeTargetWithHostTestsImpl(
    override val name: String? = null,
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<@Serializable(with = KotlinNativeHostTestRunKeyTransformingSerializer::class) KotlinNativeHostTestRun>
) : KotlinNativeTargetWithHostTests() {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests>()) { _, _ -> }
}
