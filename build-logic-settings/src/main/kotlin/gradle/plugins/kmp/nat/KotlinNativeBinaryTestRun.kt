package gradle.plugins.kmp.nat

import gradle.api.tasks.test.TestFilter
import gradle.plugins.kmp.KotlinTargetTestRun
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

internal interface KotlinNativeBinaryTestRun<T : org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun>
    : KotlinTargetTestRun<T> {
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
