package gradle.api.tasks

import gradle.api.tasks.util.PatternFilterable
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask

internal abstract class SourceTask<T : SourceTask> : ConventionTask<T>(), PatternFilterable<org.gradle.api.tasks.util.PatternFilterable> {

    abstract val sourceFiles: List<String>?

    context(Project)
    override fun applyTo(named: T) {
        super<ConventionTask>.applyTo(named)
        super<PatternFilterable>.applyTo(named)

        sourceFiles?.let { sourceFiles ->
            named.source(*sourceFiles.toTypedArray())
        }
    }
}
