package gradle.plugins.kotlin.targets.jvm

import gradle.api.tasks.copy.CopySpecImpl
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

internal interface KotlinJvmBinaryDsl {

    /**
     * The name of the application's Java module if it should run as a module.
     */
    val mainModule: String?

    /**
     * The fully qualified name of the application's main class.
     */
    val mainClass: String?

    /**
     * The name of the application.
     */
    val applicationName: String?

    /**
     * Array of string arguments to pass to the JVM when running the application.
     */
    val applicationDefaultJvmArgs: List<String>?

    /**
     * Directory to place executables in.
     *
     * The default value is "bin".
     */
    val executableDir: String?

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
    val applicationDistribution: CopySpecImpl?

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmBinaryDsl) {
        receiver.mainModule tryAssign mainModule
        receiver.mainClass tryAssign mainClass
        receiver.applicationName tryAssign applicationName
        receiver.applicationDefaultJvmArgs tryAssign applicationDefaultJvmArgs
        receiver.executableDir tryAssign executableDir
        applicationDistribution?.applyTo(receiver.applicationDistribution)
    }
}

@Serializable
internal data class KotlinJvmBinaryDslSettings(
    override val mainModule: String? = null,
    override val mainClass: String? = null,
    override val applicationName: String? = null,
    override val applicationDefaultJvmArgs: List<String>? = null,
    override val executableDir: String? = null,
    override val applicationDistribution: CopySpecImpl? = null,
    val compilationName: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    val disambiguationSuffix: String = "",
) : KotlinJvmBinaryDsl

@Serializable
internal data class KotlinJvmBinariesDsl(
    /**
     * Creates [JavaExec] task to run configured in the [KotlinJvmBinariesDsl] spec class from this target
     * compilation with name equals [compilationName].
     *
     * @param disambiguationSuffix should be used to distinguish between different executable for the same compilation.
     * This suffix is used as a last part in executable names - for example,
     * for compilation "custom" and with disambiguation suffix, "another" [JavaExec] task name will be "runJvmCustomAnother".
     */
    val executables: LinkedHashSet<KotlinJvmBinaryDslSettings>? = null,
) {

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmBinariesDsl) {
        executables?.forEach { executable ->
            receiver.executable(
                executable.compilationName,
                executable.disambiguationSuffix,
            ) {
                executable.applyTo(this)
            }
        }
    }
}

