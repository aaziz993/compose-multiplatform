package gradle.plugins.kmp.nat

import gradle.tasks.test.DefaultTestFilter
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: DefaultTestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val executionSourceFrom: NativeBuildType? = null,
    override val name: String = ""
) : KotlinNativeBinaryTestRun

internal object KotlinNativeBinaryTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeBinaryTestRunImpl>(
    KotlinNativeBinaryTestRunImpl.serializer(),
    "name",
)
