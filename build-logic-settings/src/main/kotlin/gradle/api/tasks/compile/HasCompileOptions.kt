package gradle.api.tasks.compile

import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.HasCompileOptions

internal interface HasCompileOptions<T: HasCompileOptions> {

    val options: CompileOptions?

    context(project: Project)
    fun applyTo(receiver: T) {
        options?.applyTo(hasCompileOptions.options)
    }
}
