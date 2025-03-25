package gradle.plugins.kmp.nat.android

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.plugins.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Serializable
@SerialName("androidNativeX64")
internal data class KotlinAndroidNativeX64Target(
    override val targetName: String = "androidNativeX64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNativeTarget(), KotlinAndroidNative64Target {

    context(Project)
    override fun applyTo() =
        super.applyTo(project.kotlin.targets.withType<KotlinNativeTarget>(), kotlin::androidNativeX64)
}
