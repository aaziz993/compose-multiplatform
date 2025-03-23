package gradle.plugins.kmp.nat

import gradle.api.tasks.test.DefaultTestFilter
import gradle.api.tasks.test.TestFilter
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeHostTestRun(
    override val filter: DefaultTestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeHostTestRun, org.gradle.api.tasks.testing.TestFilter>

internal object KotlinNativeHostTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.serializer(),
    "type",
)
