package gradle.plugins.android.test

import com.android.build.api.dsl.TestOptions
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import gradle.plugins.android.device.ManagedDevices
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** Options for running tests. */
@Serializable
internal data class TestOptions(
    /** Options for controlling unit tests execution. */
    val unitTests: UnitTestOptions? = null,
    /** Name of the results directory. */
    val resultsDir: String? = null,
    /** Name of the reports directory. */
    val reportDir: String? = null,
    /**
     * Disables animations during instrumented tests you run from the command line.
     *
     * If you set this property to `true`, running instrumented tests with Gradle from the command
     * line executes `am instrument` with the `--no-window-animation` flag.
     * By default, this property is set to `false`.
     *
     * This property does not affect tests that you run using Android Studio. To learn more about
     * running tests from the command line, see
     * [Test from the Command Line](https://d.android.com/studio/test/command-line.html).
     */
    val animationsDisabled: Boolean? = null,
    /**
     * Configures Gradle Managed Devices for use in testing with the Unified test platform.
     */
    val managedDevices: ManagedDevices? = null,
    /**
     * Specifies whether to use on-device test orchestration.
     *
     * If you want to [use Android Test Orchestrator](https://developer.android.com/training/testing/junit-runner.html#using-android-test-orchestrator)
     * you need to specify `"ANDROID_TEST_ORCHESTRATOR"`, as shown below.
     * By default, this property is set to `"HOST"`, which disables on-device orchestration.
     *
     * ```
     * android {
     *   testOptions {
     *     execution 'ANDROID_TEST_ORCHESTRATOR'
     *   }
     * }
     * ```
     *
     * since 3.0.0
     */
    val execution: String? = null,

    /**
     * Configures Android Emulator Grpc Access
     *
     * Android Emulator Grpc Access will make it possible to interact with the emulator over gRPC
     *
     * ```
     * android {
     *     emulatorControl {
     *       enable true
     *       secondsValid 180
     *       allowedEndpoints.addAll(
     *           "/android.emulation.control.EmulatorController/getStatus",
     *           "/android.emulation.control.EmulatorController/getVmState")
     *     }
     * }
     * ```
     */
    val emulatorControl: EmulatorControl? = null,
    /**
     * Configures Android Test Retention.
     *
     * Android Test Retention automatically takes emulator snapshots on test failures. It can only
     * work with Unified Test Platform (UTP).
     *
     * ```
     * android {
     *   testOptions {
     *     emulatorSnapshots {
     *       enableForTestFailures true
     *       maxSnapshotsForTestFailures 2
     *       compressSnapshots false
     *     }
     *   }
     * }
     * ```
     */
    val emulatorSnapshots: EmulatorSnapshots? = null,
    /**
     * Specifies value that overrides target sdk version number for tests in libraries.
     * Default value is set to minSdk.
     * Important: Setting this value will cause an error for application and other module types.
     */
    val targetSdk: Int? = null,

    /**
     * Specifies value that overrides target sdk preview number for tests in libraries.
     * Default value is empty.
     * Important: Setting this value will cause an error for application and other module types.
     */
    val targetSdkPreview: String? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: TestOptions) {
        unitTests?.applyTo(receiver.unitTests)
        receiver::resultsDir trySet resultsDir
        receiver::reportDir trySet reportDir
        receiver::animationsDisabled trySet animationsDisabled
        managedDevices?.applyTo(receiver.managedDevices)
        receiver::execution trySet execution
        emulatorControl?.applyTo(receiver.emulatorControl)
        emulatorSnapshots?.applyTo(receiver.emulatorSnapshots)
        receiver::targetSdk trySet (targetSdk ?: settings.libs.versions.version("android.targetSdk")?.toInt())
        receiver::targetSdkPreview trySet targetSdkPreview
    }
}
