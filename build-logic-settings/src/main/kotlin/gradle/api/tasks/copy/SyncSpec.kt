package gradle.api.tasks.copy

import gradle.api.tasks.util.PatternFilterable
import org.gradle.api.Project
import org.gradle.api.file.SyncSpec

/**
 * Synchronizes the contents of a destination directory with some source directories and files.
 *
 * @since 7.5
 */
internal interface SyncSpec<T : SyncSpec> : CopySpec<T> {

    /**
     * Configures the filter that defines which files to preserve in the destination directory.
     *
     * @param action Action for configuring the preserve filter
     * @return this
     */
    val preserve: PatternFilterable<org.gradle.api.tasks.util.PatternFilterable>?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        preserve?.applyTo(recipient.preserve)
    }
}
