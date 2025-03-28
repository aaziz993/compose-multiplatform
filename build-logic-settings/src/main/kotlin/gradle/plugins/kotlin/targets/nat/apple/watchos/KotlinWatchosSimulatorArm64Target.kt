package gradle.plugins.kotlin.targets.nat.apple.watchos

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainerTransformingSerializer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilationKeyTransformingSerializer
import gradle.plugins.kotlin.targets.nat.KotlinNativeSimulatorTestRun
import gradle.plugins.kotlin.targets.nat.KotlinNativeSimulatorTestRunKeyTransformingSerializer
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetWithSimulatorTests
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("watchosSimulatorArm64")
internal data class KotlinWatchosSimulatorArm64Target(
    override val name: String = "watchosSimulatorArm64",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<@Serializable(with = KotlinNativeSimulatorTestRunKeyTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinWatchos64Target, KotlinWatchosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests>()) { name, action ->
            project.kotlin.watchosSimulatorArm64(name, action::execute)
        }
}
