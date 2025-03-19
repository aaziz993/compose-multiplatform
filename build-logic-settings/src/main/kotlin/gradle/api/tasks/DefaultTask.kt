package gradle.api.tasks

import org.gradle.api.DefaultTask

internal abstract class DefaultTask<T : DefaultTask> : Task<T>
