package gradle.model.kmp.nat.linux

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kmp.nat.KotlinNativeCompilation
import gradle.model.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kmp.nat.KotlinNativeCompilerOptions
import gradle.model.kmp.nat.KotlinNativeHostTestRun
import gradle.model.kmp.nat.KotlinNativeTargetWithHostTests

@Serializable
@SerialName("linuxX64")
internal data class KotlinLinuxX64Target(
    override val targetName: String = "linuxX64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeHostTestRun>? = null,
) : KotlinNativeTargetWithHostTests(), KotlinLinuxTarget {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::linuxX64)

        super.applyTo()
    }
}
