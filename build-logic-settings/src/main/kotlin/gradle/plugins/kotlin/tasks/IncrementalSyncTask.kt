package gradle.plugins.kotlin.tasks

import gradle.accessors.files
import gradle.api.file.tryFrom
import gradle.api.file.trySetFrom
import gradle.api.tasks.Task
import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.jetbrains.kotlin.gradle.tasks.IncrementalSyncTask

/**
 * A task to incrementally synchronize a set of files between directories.
 *
 * Incremental synchronization support greatly reduces task execution time on subsequent builds when the set of files to be synchronized is large,
 * but only a small amount have changed.
 */
internal interface IncrementalSyncTask<T : IncrementalSyncTask> : Task<T> {

    /**
     * The collection of paths with files to copy.
     *
     * Should be configured using available methods in the [ConfigurableFileCollection]
     * such as [ConfigurableFileCollection.from] or [ConfigurableFileCollection.setFrom].
     *
     * @see [ConfigurableFileCollection]
     */
    val from: Set<String>?
    val setFrom: Set<String>?

    /**
     * Duplicates strategy for CopySpec inside the task
     */
    val duplicatesStrategy: DuplicatesStrategy?

    /**
     * The directory where the set of files are copied to.
     */
    val destinationDirectory: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.from tryFrom from?.let(project::files)
        receiver.from trySetFrom setFrom?.let(project::files)
        receiver::duplicatesStrategy trySet duplicatesStrategy
    }
}
