package plugin.project.kotlin.kmp.model.nat

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import plugin.project.kotlin.kmp.model.KotlinTarget

@Serializable
internal abstract class KotlinNativeTargetWithTests : KotlinNativeTarget() {

    context(Project)
    override fun applyTo() =
        super.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>())
}

@Serializable
@SerialName("nativeWithTests")
internal data class KotlinNativeTargetWithTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTargetWithTests() {

    override val targetName: String
        get() = ""
}
