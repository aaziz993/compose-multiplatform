package gradle.plugins.kmp.nat

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class NativeBinaryTestRunSource(
    val binary: NativeBuildType
) : KotlinExecution.ExecutionSource
