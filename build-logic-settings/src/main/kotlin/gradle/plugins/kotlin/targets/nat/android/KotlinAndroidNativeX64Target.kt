package gradle.plugins.kotlin.targets.nat.android

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kotlin.targets.nat.KotlinNativeBinaryContainer
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilation
import gradle.plugins.kotlin.targets.nat.KotlinNativeCompilationKeyTransformingSerializer
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("androidNativeX64")
internal data class KotlinAndroidNativeX64Target(
    override val name: String = "androidNativeX64",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNativeTarget(), KotlinAndroidNative64Target {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<KotlinNativeTarget>()) { name, action ->
            project.kotlin.androidNativeX64(name, action::execute)
        }
}
