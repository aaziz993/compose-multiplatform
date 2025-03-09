package gradle.model.kotlin.kmp.nat

import gradle.model.kotlin.CompilationExecutionSource
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.NativeBinaryTestRunSource

@Serializable
internal data class NativeBinaryTestRunSource(
    override val compilation: KotlinNativeCompilation? = null,
    val binary: TestExecutable? = null
) : CompilationExecutionSource {

    context(Project)
    fun applyTo(source: NativeBinaryTestRunSource) {
        super.applyTo(source)
        binary?.applyTo(source.binary)
    }
}
