package gradle.model.kotlin.kmp.nat

import gradle.model.kotlin.CompilationExecutionSource
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.NativeBinaryTestRunSource

@Serializable
internal data class NativeBinaryTestRunSource(
    val binary: TestExecutable? = null
) : CompilationExecutionSource {

    context(Project)
    fun applyTo(source: NativeBinaryTestRunSource) {
        binary?.applyTo(source.binary)
    }
}
