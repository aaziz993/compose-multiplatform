package gradle.plugins.kmp.nat.apple.watchos

import gradle.accessors.kotlin
import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.plugins.kmp.nat.KotlinNativeCompilation
import gradle.api.applyTo
import gradle.plugins.kmp.nat.KotlinNativeCompilationKeyTransformingSerializer
import gradle.plugins.kmp.nat.KotlinNativeTarget
import gradle.plugins.kmp.nat.tasks.KotlinNativeCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("watchosDeviceArm64")
internal data class KotlinWatchosDeviceArm64Target(
    override val name: String = "watchosDeviceArm64",
    override val compilations: LinkedHashSet<@Serializable(with = KotlinNativeCompilationKeyTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(), KotlinWatchos64Target, KotlinWatchosTarget {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()) { name, action ->
            project.kotlin.watchosDeviceArm64(name, action::execute)
        }
}
