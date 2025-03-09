package gradle.model.kotlin.kmp.nat

import gradle.model.DefaultTestFilter
import gradle.model.kotlin.kmp.KotlinTarget
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: DefaultTestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val executionSourceFrom: String? = null,
    override val name: String = ""
) : KotlinNativeBinaryTestRun

internal object KotlinNativeBinaryTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeBinaryTestRunImpl>(
    KotlinNativeBinaryTestRunImpl.serializer(),
    "name",
)
