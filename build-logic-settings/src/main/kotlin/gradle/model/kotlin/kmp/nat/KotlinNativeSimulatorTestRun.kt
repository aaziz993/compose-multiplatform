package gradle.model.kotlin.kmp.nat

import gradle.model.TestFilter
import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

@Serializable
internal data class KotlinNativeSimulatorTestRun(
    override val executionSourceFrom: String? = null,
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String,
    /**
     * Determines which simulated device will be used to execute the test run.
     * To get list of all available devices, run `xcrun simctl list`.
     */
    val deviceId: String,
) : KotlinNativeBinaryTestRun {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.targets.native.KotlinNativeSimulatorTestRun

        named::deviceId trySet deviceId
    }
}

internal object KotlinNativeSimulatorTestRunTransformingSerializer : KeyTransformingSerializer<KotlinNativeSimulatorTestRun>(
    KotlinNativeSimulatorTestRun.serializer(),
    "name",
)
