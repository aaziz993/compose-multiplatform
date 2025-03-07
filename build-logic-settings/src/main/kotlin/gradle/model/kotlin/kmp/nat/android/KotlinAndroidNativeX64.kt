package gradle.model.kotlin.kmp.nat.android

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.kotlin.kmp.nat.KotlinNativeBinaryContainer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilation
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilationTransformingSerializer
import gradle.model.kotlin.kmp.nat.KotlinNativeCompilerOptions

@Serializable
@SerialName("androidNativeX64")
internal data class KotlinAndroidNativeX64(
    override val targetName: String = "androidNativeX64",
    override val compilations: List<@Serializable(with = KotlinNativeCompilationTransformingSerializer::class) KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    override val isLeaf: Boolean
        get() = true

    context(Project)
    override fun applyTo() {
        create(kotlin::androidNativeX64)

        super.applyTo()
    }
}
