package gradle.plugins.kotlin.tasks

import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool

internal abstract class AbstractKotlinCompileTool : Task,KotlinCompileTool{

    override val destinationDirectory: DirectoryProperty
        get() =
}
