package gradle.plugins.kotlin.targets.nat

import gradle.api.NamedKeyValueTransformingSerializer
import gradle.api.tasks.test.TestFilter
import gradle.api.trySet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = KotlinNativeSimulatorTestRunKeyValueTransformingSerializer::class)
internal data class KotlinNativeSimulatorTestRun(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String,
    /**
     * Determines which simulated device will be used to execute the test run.
     * To get list of all available devices, run `xcrun simctl list`.
     */
    val deviceId: String? = null,
) : KotlinNativeBinaryTestRun<org.jetbrains.kotlin.gradle.targets.native.KotlinNativeSimulatorTestRun> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.native.KotlinNativeSimulatorTestRun) {
        super.applyTo(receiver)

        receiver::deviceId trySet deviceId
    }
}

private object KotlinNativeSimulatorTestRunKeyValueTransformingSerializer
    : NamedKeyValueTransformingSerializer<KotlinNativeSimulatorTestRun>(
    KotlinNativeSimulatorTestRun.generatedSerializer(),
)
