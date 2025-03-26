package gradle.plugins.kmp.nat.android

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.api.applyTo
import gradle.plugins.kmp.nat.KotlinNativeCompilationKeyTransformingSerializer
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("androidNativeArm32")
internal data class KotlinAndroidNativeArm32Target(
    override val name: String = "androidNativeArm32",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val onPublicationCreated: String? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: @Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class) KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNativeTarget(), KotlinAndroidNative32Target {

    context(Project) override fun applyTo() = applyTo(project.kotlin.targets.withType<KotlinNativeTarget>()) { name, action ->
        project.kotlin.androidNativeArm32(name, action::execute)
    }
}
