package plugin.project.gradle.dokka.model

internal enum class WorkerIsolation {
    /** @see WorkerIsolation.ClassLoader */
    ClassLoaderIsolation,

    /** @see WorkerIsolation.Process */
    ProcessIsolation,
}
