package gradle.tasks.compile

import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.HasCompileOptions

internal interface HasCompileOptions {

    val options: CompileOptions?

    context(Project)
    fun applyTo(hasCompileOptions: HasCompileOptions) {
        options?.applyTo(hasCompileOptions.options)
    }
}
