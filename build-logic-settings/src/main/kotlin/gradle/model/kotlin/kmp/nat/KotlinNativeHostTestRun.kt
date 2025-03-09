package gradle.model.kotlin.kmp.nat

import gradle.model.TestFilter
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeHostTestRun(
    override val executionSourceFrom: String? = null,
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun

internal object KotlinNativeHostTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.serializer(),
    "type",
)
