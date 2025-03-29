package gradle.plugins.kotlin.targets.nat.apple.macos

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.KotlinNativeHostTestRun
import gradle.plugins.kotlin.targets.nat.KotlinNativeTargetWithHostTests
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("macosArm64")
internal data class KotlinMacosArm64Target(
    override val name: String = "macosArm64",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: LinkedHashSet<KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests(), KotlinMacosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests>()) { name, action ->
            project.kotlin.macosArm64(name, action::execute)
        }
}
