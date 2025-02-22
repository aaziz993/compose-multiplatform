package plugin.project.android.model

import com.android.build.api.dsl.EmulatorSnapshots
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * Options for configuring Android Test Retention.
 *
 * When enabled, Android Test Retention automatically takes emulator snapshots on test failures.
 */
@Serializable
internal data class EmulatorSnapshots(
    /** Enables automated test failure snapshots. Default to false. */
    var enableForTestFailures: Boolean? = null
    /**
     * Call this function to take unlimited number of test failure snapshots (will ignore
     * maxSnapshotsForTestFailures setting)
     */
    val retainAll: Boolean? = null,
    /**
     *  Maximum number of failures that would be snapshotted. Any failures after the first
     *  $maxSnapshotsForTestFailures will not have snapshots. Default to 2. Must be >0
     */
    var maxSnapshotsForTestFailures: Int? = null,
    /** Enables snapshot compression. Default to false. */
    var compressSnapshots: Boolean? = null
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(snapshots: EmulatorSnapshots) {
        snapshots::enableForTestFailures trySet enableForTestFailures
        retainAll?.takeIf { it }?.run { snapshots.retainAll() }
        snapshots::maxSnapshotsForTestFailures trySet maxSnapshotsForTestFailures
        snapshots::compressSnapshots trySet compressSnapshots
    }
}
