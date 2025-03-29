package gradle.api.tasks

import gradle.accessors.files
import gradle.api.tasks.util.PatternFilterable
import gradle.api.trySet
import gradle.api.trySetArray
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask

internal abstract class SourceTask<T : SourceTask> : ConventionTask<T>(), PatternFilterable<T> {

    abstract val sourceFiles: Set<String>?
    abstract val setSourceFiles: Set<String>?

    context(Project)
    override fun applyTo(receiver: T) {
        super<ConventionTask>.applyTo(receiver)
        super<PatternFilterable>.applyTo(receiver)

        receiver::source trySetArray sourceFiles
        receiver::setSource trySet setSourceFiles?.let(project::files)?.asFileTree
    }
}
