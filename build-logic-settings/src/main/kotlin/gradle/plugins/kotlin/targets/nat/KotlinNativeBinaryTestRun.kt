package gradle.plugins.kotlin.targets.nat

import gradle.api.NamedKeyTransformingSerializer
import gradle.api.tasks.test.TestFilter
import gradle.plugins.kotlin.KotlinTargetTestRun
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests

internal interface KotlinNativeBinaryTestRun<T : org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>
    : KotlinTargetTestRun<org.jetbrains.kotlin.gradle.targets.native.NativeBinaryTestRunSource, T> {

    override val executionSource: NativeBinaryTestRunSource?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        val target = receiver.target as KotlinNativeTargetWithTests<*>

        executionSource?.let { executionSource ->
            when (executionSource.binary) {
                is String -> receiver.setExecutionSourceFrom(
                    target.binaries.getTest(executionSource.binary),
                )

                is Binary -> receiver.setExecutionSourceFrom(
                    target.binaries.getTest(executionSource.binary.namePrefix, executionSource.binary.buildType),
                )

                else -> Unit
            }
        }
    }
}

internal abstract class KotlinNativeBinaryTestRunKeyTransformingSerializer<T : KotlinNativeBinaryTestRun<*>>(
    tSerializer: KSerializer<T>
) : NamedKeyTransformingSerializer<T>(tSerializer)

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String? = null,
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>

internal object KotlinNativeBinaryTestRunKeyImplTransformingSerializer : KotlinNativeBinaryTestRunKeyTransformingSerializer<KotlinNativeBinaryTestRunImpl>(
    KotlinNativeBinaryTestRunImpl.serializer(),
)
