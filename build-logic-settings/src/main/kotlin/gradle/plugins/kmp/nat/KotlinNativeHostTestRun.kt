package gradle.plugins.kmp.nat

import gradle.tasks.test.TestFilter
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class KotlinNativeHostTestRun(
    override val executionSourceFrom: NativeBuildType? = null,
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun

internal object KotlinNativeHostTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.serializer(),
    "type",
)
