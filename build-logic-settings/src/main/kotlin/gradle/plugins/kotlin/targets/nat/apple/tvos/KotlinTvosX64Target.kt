package gradle.plugins.kotlin.targets.nat.apple.tvos

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.KotlinNativeSimulatorTestRun
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetWithSimulatorTests
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("tvosX64")
internal data class KotlinTvosX64Target(
    override val name: String = "tvosX64",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<KotlinNativeSimulatorTestRun>? = null,
) : KotlinNativeTargetWithSimulatorTests(), KotlinTvosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests>()) { name, action ->
            project.kotlin.tvosX64(name, action::execute)
        }
}
