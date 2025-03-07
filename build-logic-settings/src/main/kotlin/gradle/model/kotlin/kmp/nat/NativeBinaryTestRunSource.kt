package gradle.model.kotlin.kmp.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.NativeBinaryTestRunSource
import gradle.model.kotlin.CompilationExecutionSource

@Serializable
internal data class NativeBinaryTestRunSource(
    val binary: TestExecutable? = null
) : CompilationExecutionSource {

    context(Project)
    fun applyTo(source: NativeBinaryTestRunSource) {
        binary?.applyTo(source.binary)
    }
}
