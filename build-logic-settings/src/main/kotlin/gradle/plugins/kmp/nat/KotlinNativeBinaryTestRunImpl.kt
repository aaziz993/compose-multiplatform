package gradle.plugins.kmp.nat

import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.tasks.test.DefaultTestFilter
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: DefaultTestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String = ""
) : KotlinNativeBinaryTestRun

internal object KotlinNativeBinaryTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeBinaryTestRunImpl>(
    KotlinNativeBinaryTestRunImpl.serializer(),
    "name",
)
