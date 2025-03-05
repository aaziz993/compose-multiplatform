package plugin.project.kotlin.kmp.model.nat.apple.watchos

import gradle.containerize
import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import plugin.project.kotlin.kmp.model.nat.KotlinNativeBinaryContainer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilation
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilationTransformingSerializer
import plugin.project.kotlin.kmp.model.nat.KotlinNativeCompilerOptions

@Serializable
@SerialName("watchosSimulatorArm64")
internal data class KotlinWatchosSimulatorArm64Target(
    override val targetName: String = "watchosSimulatorArm64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinWatchosTarget() {

    context(Project)
    override fun applyTo() {
        create(kotlin::watchosSimulatorArm64)

        super.applyTo()
    }
}
