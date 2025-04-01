package gradle.plugins.java.test

import gradle.accessors.files
import klib.data.type.collection.SerializableAnyMap
import klib.data.type.collection.tryAddAll
import gradle.process.ProcessForkOptions
import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.process.JavaForkOptions

/**
 *
 * Specifies the options to use to fork a Java process.
 */
internal interface JavaForkOptions<T : JavaForkOptions> : ProcessForkOptions<T> {

    /**
     * Adds some system properties to use for the process.
     *
     * @param properties The system properties. Must not be null.
     * @return this
     */
    val systemProperties: SerializableAnyMap?
    val setSystemProperties: SerializableAnyMap?

    /**
     * Sets the default character encoding to use.
     *
     * Note: Many JVM implementations support the setting of this attribute via system property on startup (namely, the `file.encoding` property). For JVMs
     * where this is the case, setting the `file.encoding` property via [.setSystemProperties] or similar will have no effect as
     * this value will be overridden by the value specified by [.getDefaultCharacterEncoding].
     *
     * @param defaultCharacterEncoding The default character encoding. Use null to use [this JVM&#39;s default charset][java.nio.charset.Charset.defaultCharset]
     */
    val defaultCharacterEncoding: String?

    /**
     * Sets the minimum heap size for the process.
     * Supports the units megabytes (e.g. "512m") and gigabytes (e.g. "1g").
     *
     * @param heapSize The minimum heap size. Use null for the default minimum heap size.
     */
    val minHeapSize: String?

    /**
     * Sets the maximum heap size for the process.
     * Supports the units megabytes (e.g. "512m") and gigabytes (e.g. "1g").
     *
     * @param heapSize The heap size. Use null for the default maximum heap size.
     */
    val maxHeapSize: String?

    /**
     * Sets the extra arguments to use to launch the JVM for the process. System properties
     * and minimum/maximum heap size are updated.
     *
     * @param arguments The arguments. Must not be null.
     * @since 4.0
     */
    val jvmArgs: List<String>?
    val setJvmArgs: List<String>?

    /**
     * Adds the given values to the end of the bootstrap classpath for the process.
     *
     * @param classpath The classpath.
     * @return this
     */
    val bootstrapClasspath: List<String>?
    val setBootstrapClasspath: List<String>?

    /**
     * Enable or disable assertions for the process.
     *
     * @param enabled true to enable assertions, false to disable.
     */
    val enableAssertions: Boolean?

    /**
     * Enable or disable debugging for the process. When enabled, the process is started suspended and listening on port
     * 5005.
     *
     *
     * The debug properties (e.g. the port number) can be configured in [.debugOptions].
     *
     * @param enabled true to enable debugging, false to disable.
     */
    val debug: Boolean?

    /**
     * Configures Java Debug Wire Protocol properties for the process. If [.setDebug] is enabled then
     * the `-agentlib:jdwp=...`  will be appended to the JVM arguments with the configuration from the parameter.
     *
     * @param action the Java debug configuration
     * @since 5.6
     */
    val debugOptions: JavaDebugOptions?

    /**
     * Sets the full set of arguments to use to launch the JVM for the process. Overwrites any previously set system
     * properties, minimum/maximum heap size, assertions, and bootstrap classpath.
     *
     * @param arguments The arguments. Must not be null.
     * @since 4.0
     */
    val allJvmArgs: List<String>?
    val setAllJvmArgs: List<String>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::systemProperties trySet systemProperties
        receiver::setSystemProperties trySet setSystemProperties
        receiver::setDefaultCharacterEncoding trySet defaultCharacterEncoding
        receiver::setMinHeapSize trySet minHeapSize
        receiver::setMaxHeapSize trySet maxHeapSize
        jvmArgs?.let(receiver::jvmArgs)
        receiver::setJvmArgs trySet setJvmArgs

        receiver::bootstrapClasspath trySet bootstrapClasspath

        receiver::setBootstrapClasspath trySet setBootstrapClasspath?.let(project::files)

        receiver::setEnableAssertions trySet enableAssertions
        receiver::setDebug trySet debug
        debugOptions?.applyTo(receiver.debugOptions)
        receiver.allJvmArgs tryAddAll allJvmArgs
        receiver::setAllJvmArgs trySet allJvmArgs
    }
}
