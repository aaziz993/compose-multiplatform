@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.gradle

import org.gradle.api.Project
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME

internal fun Project.ideaImportDependOn(vararg paths: Any)= tasks.configureEach {
    if (name == IDEA_IMPORT_TASK_NAME) {
        dependsOn(paths)
    }
}
