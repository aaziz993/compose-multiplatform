package gradle.plugins.kmp.nat

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable

@Serializable
internal data class NativeBinaryTestRunSource(
    val binary: @Serializable(with = BinaryContentPolymorphicSerializer::class) Any,
) : KotlinExecution.ExecutionSource
