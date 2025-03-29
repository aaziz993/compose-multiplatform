package gradle.plugins.kotlin.targets.nat.apple.watchos

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.KotlinNativeTarget
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("watchosDeviceArm64")
internal data class KotlinWatchosDeviceArm64Target(
    override val name: String = "watchosDeviceArm64",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(), KotlinWatchos64Target, KotlinWatchosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()) { name, action ->
            project.kotlin.watchosDeviceArm64(name, action::execute)
        }
}
