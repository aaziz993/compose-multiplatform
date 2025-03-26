package gradle.plugins.kmp.nat

import gradle.api.tasks.test.TestFilter
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeHostTestRun(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeHostTestRun>

internal object KotlinNativeHostTestRunKeyTransformingSerializer : KotlinNativeBinaryTestRunKeyTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.serializer(),
)
