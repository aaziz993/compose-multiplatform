package gradle.plugins.kmp.nat

import gradle.api.tasks.test.TestFilter
import gradle.plugins.kmp.KotlinTargetTestRun
import gradle.plugins.kotlin.KotlinExecution
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests

internal interface KotlinNativeBinaryTestRun<T : org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>
    : KotlinTargetTestRun<T> {

    override val executionSource: NativeBinaryTestRunSource?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        val target = receiver.target as KotlinNativeTargetWithTests<*>

        when (val binary = executionSource.binary) {
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

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String? = null,
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>

internal object KotlinNativeBinaryTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeBinaryTestRunImpl>(
    KotlinNativeBinaryTestRunImpl.serializer(),
    "name",
)
