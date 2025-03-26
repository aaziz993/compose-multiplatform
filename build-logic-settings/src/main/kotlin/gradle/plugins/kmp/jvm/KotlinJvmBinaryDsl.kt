package gradle.plugins.kmp.jvm

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinJvmBinaryDsl (
    /**
     * The name of the application's Java module if it should run as a module.
     */
    val mainModule: Property<String>

    /**
     * The fully qualified name of the application's main class.
     */
    val mainClass: Property<String>

    /**
     * The name of the application.
     */
    val applicationName: Property<String>

    /**
     * Array of string arguments to pass to the JVM when running the application.
     */
    val applicationDefaultJvmArgs: ListProperty<String>

    /**
     * Directory to place executables in.
     *
     * The default value is "bin".
     */
    val executableDir: Property<String>

    /**
     * The specification of the contents of the distribution.
     *
     * Use this [CopySpec] to include extra files/resource in the application distribution:
     * ```
     * kotlin {
     *    jvm {
     *      binaries {
     *         executable {
     *            mainClass.set("foo.MainKt")
     *            applicationDistribution.from("some/dir") {
     *                it.include("*.txt")
     *            }
     *         }
     *      }
     *    }
     *  }
     * ```
     *
     * Note that the application plugin pre-configures this spec to include the contents of "src/dist",
     * copy the application start scripts into the "bin" directory,
     * and copy the built jar and its dependencies into the "lib" directory.
     */
    var applicationDistribution: CopySpec
)
