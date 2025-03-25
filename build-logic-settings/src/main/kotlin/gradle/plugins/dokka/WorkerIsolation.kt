package gradle.plugins.dokka

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.process.JavaForkOptions
import org.gradle.workers.WorkerExecutor

@Serializable
internal sealed class WorkerIsolation {

    context(Project)
    abstract fun toWorkerIsolation(): org.jetbrains.dokka.gradle.workers.WorkerIsolation
}

/**
 * Execute a Worker in the current Gradle process, with an
 * [isolated classpath][WorkerExecutor.classLoaderIsolation].
 *
 * Presently there are no options to configure the behaviour of a classloader-isolated worker.
 *
 * @see org.gradle.workers.ClassLoaderWorkerSpec
 * @see WorkerExecutor.classLoaderIsolation
 */
@Serializable
internal class ClassLoader : WorkerIsolation() {

    context(Project)
    override fun toWorkerIsolation(): org.jetbrains.dokka.gradle.workers.WorkerIsolation =
        dokka.ClassLoaderIsolation()
}

/**
 * Create a Worker using [process isolation][WorkerExecutor.processIsolation].
 *
 * Gradle will launch
 * [new Worker Daemon](https://docs.gradle.org/8.10/userguide/worker_api.html#worker-daemons),
 * re-using it across builds.
 *
 * @see org.gradle.workers.ProcessWorkerSpec
 * @see WorkerExecutor.processIsolation
 */
@Serializable
internal data class Process(
    /** @see JavaForkOptions.setDebug */
    val debug: Boolean? = null,

    /** @see JavaForkOptions.setEnableAssertions */
    val enableAssertions: Boolean? = null,

    /** @see JavaForkOptions.setMinHeapSize */
    val minHeapSize: String? = null,

    /** @see JavaForkOptions.setMaxHeapSize */
    val maxHeapSize: String? = null,

    /** @see JavaForkOptions.setJvmArgs */
    val jvmArgs: List<String>? = null,

    /** @see JavaForkOptions.setDefaultCharacterEncoding */
    val defaultCharacterEncoding: String? = null,

    /** @see JavaForkOptions.setSystemProperties */
    val systemProperties: SerializableAnyMap? = null,
) : WorkerIsolation() {

    context(Project)
    override fun toWorkerIsolation(): org.jetbrains.dokka.gradle.workers.WorkerIsolation =
        dokka.ProcessIsolation {
            debug tryAssign this@Process.debug
            enableAssertions tryAssign this@Process.enableAssertions
            minHeapSize tryAssign this@Process.minHeapSize
            maxHeapSize tryAssign this@Process.maxHeapSize
            jvmArgs tryAssign this@Process.jvmArgs
            defaultCharacterEncoding tryAssign this@Process.defaultCharacterEncoding
            systemProperties tryAssign this@Process.systemProperties
        }
}
