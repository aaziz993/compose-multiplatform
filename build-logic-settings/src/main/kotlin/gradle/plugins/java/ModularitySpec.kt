package gradle.plugins.java

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.jvm.ModularitySpec

/**
 * Description of the modularity of a classpath.
 *
 *
 * A classpath is a list of JAR files, classes and resources folders passed to one of the JDK's commands like javac, java, or javadoc.
 * Since Java 9, these commands offer two different approaches to handle such a classpath:
 *
 *
 *  * A traditional classpath by using the --classpath parameter.
 *  * A modular classpath (module path) using the --module-path parameter.
 *
 *
 * If the --module-path is used, the Java Platform Module System (JPMS) becomes active and the module descriptors (module-info.class)
 * files are used to limit visibility of packages between modules at compile and runtime.
 *
 *
 * Wherever a classpath (a list of files and folders) is configured in Gradle, it can be accompanied by a ModularClasspathHandling object
 * to describe how the entries of that list are to be passed to the --classpath and --module-path parameters.
 *
 * @since 6.4
 */
@Serializable
internal data class ModularitySpec(
    /**
     * Should a --module-path be inferred by analysing JARs and class folders on the classpath?
     *
     *
     * An entry is considered to be part of the module path if it meets one of the following conditions:
     *
     *  * It is a jar that contains a 'module-info.class'.
     *  * It is a class folder that contains a 'module-info.class'.
     *  * It is a jar with a MANIFEST that contains an 'Automatic-Module-Name' entry.
     *
     */
    val inferModulePath: Boolean? = null,
) {

    fun applyTo(spec: ModularitySpec) {
        spec.inferModulePath tryAssign inferModulePath
    }
}
