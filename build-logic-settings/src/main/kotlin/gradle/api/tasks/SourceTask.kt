package gradle.api.tasks

import gradle.api.tasks.util.PatternFilterable
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask

internal abstract class SourceTask<T : SourceTask> : ConventionTask<T>(), PatternFilterable<T> {

    abstract val sourceFiles: List<String>?

    context(Project)
    override fun applyTo(recipient: T) {
        super<ConventionTask>.applyTo(recipient)
        super<PatternFilterable>.applyTo(recipient)

        sourceFiles?.let { sourceFiles ->
            recipient.source(*sourceFiles.toTypedArray())
        }
    }
}
