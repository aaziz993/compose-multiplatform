package gradle.plugins.kotlin.tasks

import gradle.api.tasks.DefaultTask
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool

internal abstract class AbstractKotlinCompileTool<T : AbstractKotlinCompileTool<*>> : DefaultTask<T>(), KotlinCompileTool<T>
