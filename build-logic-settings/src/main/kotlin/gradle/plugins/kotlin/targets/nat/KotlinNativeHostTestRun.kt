package gradle.plugins.kotlin.targets.nat

import gradle.api.NamedMapTransformingSerializer
import gradle.api.tasks.test.TestFilter
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = KotlinNativeHostTestRunMapTransformingSerializer::class)
internal data class KotlinNativeHostTestRun(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeHostTestRun>

private object KotlinNativeHostTestRunMapTransformingSerializer : NamedMapTransformingSerializer<KotlinNativeHostTestRun>(
    KotlinNativeHostTestRun.generatedSerializer(),
)
