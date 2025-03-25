package gradle.plugins.kmp.nat.linux

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.plugins.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeTarget
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("linuxArm64")
internal data class KotlinLinuxArm64Target(
    override val targetName: String = "linuxArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget(), KotlinLinuxTarget {

    context(Project)
    override fun applyTo() =
        super.applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(), kotlin::linuxArm64)
}
