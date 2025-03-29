package gradle.plugins.kotlin.targets.nat.android

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("androidNativeX86")
internal data class KotlinAndroidNativeX86Target(
    override val name: String = "androidNativeX86",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNativeTarget(), KotlinAndroidNative32Target {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinNativeTarget>()) { name, action ->
            project.kotlin.androidNativeX86(name, action::execute)
        }
}
