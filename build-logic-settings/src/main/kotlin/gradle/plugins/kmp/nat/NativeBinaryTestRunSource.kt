package gradle.plugins.kmp.nat

import gradle.plugins.kotlin.CompilationExecutionSource
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.NativeBinaryTestRunSource

@Serializable
internal data class NativeBinaryTestRunSource(
    override val compilation: KotlinNativeCompilation? = null,
    val binary: TestExecutableSettings? = null
) : CompilationExecutionSource {

    context(Project)
    fun applyTo(source: NativeBinaryTestRunSource) {
        super.applyTo(source)
        binary?.applyTo(source.binary)
    }
}
