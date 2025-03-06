package plugin.project.kotlin.kmp.model.nat

import gradle.trySet
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeSimulatorTestRun

internal interface KotlinNativeSimulatorTestRun : KotlinNativeBinaryTestRun {

    /**
     * Determines which simulated device will be used to execute the test run.
     * To get list of all available devices, run `xcrun simctl list`.
     */
    val deviceId: String

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinNativeSimulatorTestRun

        named::deviceId trySet deviceId
    }
}
