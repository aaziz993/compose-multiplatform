package gradle.plugins.java

import kotlinx.serialization.Serializable
import org.gradle.api.plugins.JavaResolutionConsistency

/**
 * Dependency resolution consistency configuration for
 * the Java derived plugins.
 *
 * @since 6.8
 */
@Serializable
internal data class JavaResolutionConsistency(
    /**
     * Configures the runtime classpath of every source set to be consistent
     * with the compile classpath. For dependencies which are common between
     * the compile classpath and the runtime classpath, the version from the
     * compile classpath is going to be used.
     *
     * Unless you have a good reason to, this option should be preferred to
     * [.useRuntimeClasspathVersions] for different reasons:
     *
     *
     *  * As code is compiled first against the given dependencies,
     * it is expected that the versions at runtime would be the same.
     *
     *  * It avoids resolving the runtime classpath in case of a compile error.
     *
     *
     *
     * In addition, the test compile classpath is going to be configured to
     * be consistent with the main compile classpath.
     */
    val useCompileClasspathVersions: Boolean? = null,

    /**
     * Configures the compile classpath of every source set to be consistent
     * with the runtime classpath. For dependencies which are common between
     * the compile classpath and the runtime classpath, the version from the
     * runtime classpath is going to be used.
     *
     * In addition, the test runtime classpath is going to be configured to
     * be consistent with the main runtime classpath.
     *
     * Prefer [.useCompileClasspathVersions] unless you have special
     * requirements at runtime.
     */
    val useRuntimeClasspathVersions: Boolean? = null
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: JavaResolutionConsistency) {
        useCompileClasspathVersions?.takeIf { it }?.run { resolutionConsistency.useCompileClasspathVersions() }
        useRuntimeClasspathVersions?.takeIf { it }?.run { resolutionConsistency.useRuntimeClasspathVersions() }
    }
}
