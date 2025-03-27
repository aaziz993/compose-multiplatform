package gradle.plugins.kotlin.targets.nat.apple.ios

import gradle.accessors.kotlin
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilationKeyTransformingSerializer
import gradle.plugins.kotlin.targets.nat.KotlinNativeSimulatorTestRun
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetWithSimulatorTests
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("iosX64")
internal data class KotlinIosX64Target(
    override val name: String = "iosX64",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
    override val testRuns: List<@Serializable(with = KotlinNativeSimulatorTestRunKeyTransformingSerializer::class) KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinIosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinNativeTarget>()) { name, action ->
            project.kotlin.iosX64(name, action::execute)
        }
}
