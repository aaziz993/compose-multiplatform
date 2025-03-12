package gradle.tasks.copy

import gradle.tasks.PatternFilterable
import org.gradle.api.file.CopySourceSpec
import org.gradle.api.file.SyncSpec

/**
 * Synchronizes the contents of a destination directory with some source directories and files.
 *
 * @since 7.5
 */
internal interface SyncSpec : CopySpec {

    /**
     * Configures the filter that defines which files to preserve in the destination directory.
     *
     * @param action Action for configuring the preserve filter
     * @return this
     */
    val preserve: PatternFilterable?

    override fun applyTo(spec: CopySourceSpec) {
        super.applyTo(spec)

        spec as SyncSpec

        preserve?.applyTo(spec.preserve)
    }
}
