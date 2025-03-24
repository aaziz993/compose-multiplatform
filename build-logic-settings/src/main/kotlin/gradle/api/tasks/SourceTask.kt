package gradle.api.tasks

import gradle.api.tasks.util.PatternFilterable
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask

internal abstract class SourceTask<T : SourceTask> : ConventionTask<T>(), PatternFilterable<T> {

    abstract val sourceFiles: List<String>?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<ConventionTask>.applyTo(receiver)
        super<PatternFilterable>.applyTo(receiver)

        sourceFiles?.toTypedArray()?.let(receiver::setSource)
    }
}
