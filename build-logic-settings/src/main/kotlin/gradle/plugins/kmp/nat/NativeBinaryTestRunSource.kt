package gradle.plugins.kmp.nat

import gradle.plugins.kotlin.KotlinExecution
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun

@Serializable
internal data class NativeBinaryTestRunSource(
    val binary: @Serializable(with = BinaryContentPolymorphicSerializer::class) Any,
) : KotlinExecution.ExecutionSource<KotlinNativeBinaryTestRun> {

    context(Project)
    override fun applyTo(receiver: KotlinNativeBinaryTestRun) {
        val target = receiver.target as KotlinNativeTargetWithTests<*>

        when (binary) {
            is String -> receiver.setExecutionSourceFrom(
                target.binaries.getTest(binary),
            )

            is Binary -> receiver.setExecutionSourceFrom(
                target.binaries.getTest(binary.namePrefix, binary.buildType),
            )

            else -> Unit
        }
    }
}
