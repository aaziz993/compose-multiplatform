package gradle.plugins.kmp.nat

import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.tasks.test.TestFilter
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeHostTestRun(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun

internal object KotlinNativeHostTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.serializer(),
    "type",
)
