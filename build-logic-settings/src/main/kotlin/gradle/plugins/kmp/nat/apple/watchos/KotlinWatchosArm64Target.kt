package gradle.plugins.kmp.nat.apple.watchos

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
@SerialName("watchosArm64")
internal data class KotlinWatchosArm64Target(
    override val targetName: String = "watchosArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget(), KotlinWatchos32Target, KotlinWatchosTarget {

    context(project: Project)
    override fun applyTo() =
        super.applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(), kotlin::watchosArm64)
}
