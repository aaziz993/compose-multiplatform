package gradle.api.tasks.compile

import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.HasCompileOptions

internal interface HasCompileOptions<in T: HasCompileOptions> {

    val options: CompileOptions?

    context(Project)
    fun applyTo(hasCompileOptions: T) {
        options?.applyTo(hasCompileOptions.options)
    }
}
