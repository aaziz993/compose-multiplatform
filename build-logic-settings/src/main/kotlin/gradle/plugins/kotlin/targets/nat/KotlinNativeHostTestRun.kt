package gradle.plugins.kotlin.targets.nat

import gradle.api.tasks.test.TestFilter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = KotlinNativeHostTestRunKeyValueTransformingSerializer::class)
internal data class KotlinNativeHostTestRun(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeHostTestRun>

private object KotlinNativeHostTestRunKeyValueTransformingSerializer : KotlinNativeBinaryTestRunKeyValueTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.generatedSerializer(),
)
