package plugin.project.kmp.model.nat

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import plugin.project.kotlin.kmp.model.KotlinTargetWithTests

@Serializable
internal abstract class KotlinNativeTargetWithTests<T : KotlinNativeBinaryTestRun> :
    KotlinNativeTarget(), KotlinTargetWithTests<NativeBinaryTestRunSource, T> {

    context(Project)
    override fun applyTo(named: Named) =
        super<KotlinTargetWithTests>.applyTo(named)

    context(Project)
    override fun applyTo() =
        super<KotlinNativeTarget>.applyTo(kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests<*>>())
}

@Serializable
@SerialName("nativeWithTests")
internal data class KotlinNativeTargetWithTestsImpl(
    override val compilations: List<KotlinNativeCompilation>? = null,
    override val targetName: String, override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
    override val testRuns: List<KotlinNativeBinaryTestRunImpl>? = null,
) : KotlinNativeTargetWithTests<KotlinNativeBinaryTestRun>()

