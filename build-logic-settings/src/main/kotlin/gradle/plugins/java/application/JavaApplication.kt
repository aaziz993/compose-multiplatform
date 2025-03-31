package gradle.plugins.java.application

import gradle.accessors.javaApp
import gradle.api.provider.tryAssign
import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

/**
 * Configuration for a Java application, defining how to assemble the application.
 * <p>
 * An instance of this type is added as a project extension by the Java application plugin
 * under the name 'application'.
 *
 * <pre class='autoTested'>
 * plugins {
 *     id 'application'
 * }
 *
 * application {
 *   mainClass.set("com.foo.bar.FooBar")
 * }
 * </pre>
 *
 * @since 4.10
 */
@Serializable
internal data class JavaApplication(
    /**
     * The name of the application.
     */
    val applicationName: String? = null,
    /**
     * The name of the application's Java module if it should run as a module.
     *
     * @since 6.4
     */
    val mainModule: String? = null,
    /**
     * The fully qualified name of the application's main class.
     *
     * @since 6.4
     */
    val mainClass: String? = null,
    /**
     * Array of string arguments to pass to the JVM when running the application
     */
    val applicationDefaultJvmArgs: List<String>? = null,
    /**
     * Directory to place executables in
     */
    val executableDir: String? = null,
    /**
     *
     * The specification of the contents of the distribution.
     *
     *
     * Use this [org.gradle.api.file.CopySpec] to include extra files/resource in the application distribution.
     * <pre class='autoTested'>
     * plugins {
     * id 'application'
     * }
     *
     * application {
     * applicationDistribution.from("some/dir") {
     * include "*.txt"
     * }
     * }
    </pre> *
     *
     *
     * Note that the application plugin pre configures this spec to; include the contents of "`src/dist`",
     * copy the application start scripts into the "`bin`" directory, and copy the built jar and its dependencies
     * into the "`lib`" directory.
     */
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME) {
            project.javaApp::setApplicationName trySet applicationName
            project.javaApp.mainModule tryAssign mainModule
            project.javaApp.mainClass tryAssign mainClass
            project.javaApp::setApplicationDefaultJvmArgs trySet applicationDefaultJvmArgs
            project.javaApp::setExecutableDir trySet executableDir
        }
}
