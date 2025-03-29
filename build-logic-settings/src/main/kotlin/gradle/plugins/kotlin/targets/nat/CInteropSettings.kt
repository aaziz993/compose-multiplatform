package gradle.plugins.kotlin.targets.nat

import gradle.accessors.files
import gradle.api.ProjectNamed
import gradle.api.tryPlus
import gradle.api.trySet
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project

/**
 * # C interoperability settings
 *
 * This interface represents the configuration settings for invoking the [cinterop tool](https://kotlinlang.org/docs/native-c-interop.html)
 * in Kotlin/Native projects.
 * The cinterop tool provides the ability to use C libraries inside Kotlin projects.
 *
 * **Important:** Use the [CInteropSettings] API instead of directly accessing tasks, configurations,
 * and other related domain objects through the Gradle API.
 *
 * ## Example
 *
 * Here is an example of how to use [CInteropSettings] to configure a cinterop task for the `linuxX64` target:
 *
 * ```kotlin
 * //build.gradle.kts
 * kotlin {
 *     linuxX64() {
 *         compilations.getByName("main") {
 *             cinterops {
 *                 val cinteropForLinuxX64 by creating {
 *                      // Configure the CInteropSettings here
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * In this example, we've added a `cinterop` setting named `cinteropForLinuxX64` to the `linuxX64` `main` [KotlinCompilation].
 * These settings are used to create and configure a `cinterop` task, along with the necessary dependencies for the compile task.
 */
internal interface CInteropSettings<T : org.jetbrains.kotlin.gradle.plugin.CInteropSettings> : ProjectNamed<T> {

    /**
     * The collection of libraries used for building during the C interoperability process.
     * It's equivalent to the `-library`/`-l` cinterop tool options.
     */
    val dependencyFiles: Set<String>?
    val setDependencyFiles: Set<String>?

    /**
     * Specifies the path to the `.def` file that declares bindings for the C libraries.
     * This function serves as a setter for the `.def` file path and is equivalent to passing `-def` to the cinterop tool.
     *
     * #### Usage example
     *
     * The example below shows how to set a custom `.def` file path in a `build.gradle.kts` file:
     *
     * ```kotlin
     * //build.gradle.kts
     * kotlin {
     *     linuxX64 {
     *         compilations.getByName("main").cinterops.create("customCinterop") {
     *             defFile(project.file("custom.def"))
     *         }
     *     }
     *}
     *```
     *
     * In the example, the `custom.def` file located in the project directory is set as the `.def` file.
     *
     * @param file The path to the `.def` file to be used for C interoperability.
     * **Default value:** `src/nativeInterop/cinterop/{name_of_the_cinterop}.def`
     */
    val defFile: String?

    /**
     * Defines the package name for the generated bindings.
     * It is equivalent to passing `-pkg` to the cinterop tool.
     *
     * #### Example
     *
     * ```kotlin
     * //build.gradle.kts
     *kotlin {
     *    linuxX64 {
     *        compilations.getByName("main").cinterops.create("customCinterop") {
     *            defFile(project.file("custom.def"))
     *            packageName = "com.test.cinterop"
     *        }
     *    }
     *}
     * ```
     *
     * In the example, the com.test.cinterop package contains all the declarations collected from headers by the cinterop tool.
     * These declarations are then available in this package during the final Kotlin compilation.
     *
     * @param value The package name to be assigned.
     */
    val packageName: String?

    /**
     * Adds header files to produce Kotlin bindings.
     * It's equivalent to the `-header` cinterop tool option.
     *
     * #### Usage example
     *
     * ```kotlin
     * kotlin {
     *     linuxX64() {
     *         compilations.getByName("main") {
     *             cinterops {
     *                 val cinterop by creating {
     *                     defFile(project.file("custom. def"))
     *                     headers(project.file("custom.h"))
     *                 }
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * @param files The header files to be included for interoperability with C.
     * @see [header]
     */
    val headers: Set<String>?

    /**
     * A collection of directories to search for headers.
     *
     * #### Usage example
     *
     * The following example demonstrates how to add multiple directories containing header files in a `build.gradle.kts` file:
     *
     * ```kotlin
     * //build.gradle.kts
     * kotlin {
     *     linuxX64() {
     *         compilations.getByName("main") {
     *             cinterops {
     *                 val cinterop by creating {
     *                     defFile(project.file("custom.def"))
     *                     includeDirs(project.file("include/libs"))
     *                 }
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * In the example, the directory `include/libs` is specified as the prefix for the directories listed in the `headers`
     * declared in the `custom.def`.
     *
     * @param values The directories to be included.
     */
    val includeDirs: @Serializable(with = IncludeDirContentPolymorphicSerializer::class) Any?

    /**
     * The options that are passed to the compiler by the cinterop tool.
     *
     * #### Usage example
     *
     * ```kotlin
     * kotlin {
     *     linuxX64() {
     *         compilations.getByName("main") {
     *             cinterops {
     *                 val cinterop by creating {
     *                     defFile(project.file("custom.def"))
     *                     compilerOpts("-Ipath/to/headers")
     *                 }
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * In the example, the `-Ipath/to/headers` compiler option is passed to the cinterop tool.
     *
     * @param values compiler options
     */
    val compilerOpts: List<String>?

    /**
     * Adds additional linker options.
     * It's equivalent to the `-linker-options` cinterop tool option.
     *
     * #### Usage example
     *
     * ```kotlin
     * kotlin {
     *     linuxX64() {
     *         compilations.getByName("main") {
     *             cinterops {
     *                 val cinterop by creating {
     *                     defFile(project.file("custom. def"))
     *                     linkerOpts("-lNativeBase64")
     *                 }
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * @param values linker options
     */
    val linkerOpts: List<String>?

    /**
     * Adds additional options that are passed to the cinterop tool.
     *
     * #### Usage example
     *
     * ```kotlin
     * kotlin {
     *     linuxX64() {
     *         compilations.getByName("main") {
     *             cinterops {
     *                 val cinterop by creating {
     *                     defFile(project.file("custom. def"))
     *                     extraOpts("-nopack")
     *                 }
     *             }
     *         }
     *     }
     * }
     * ```
     *
     * @param values Additional cinterop tool options that are not present in [CInteropSettings]
     */
    val extraOpts: List<String>?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::dependencyFiles tryPlus dependencyFiles?.let(project::files)
        receiver::dependencyFiles trySet setDependencyFiles?.let(project::files)

        when (val includeDirs = includeDirs) {
            is String -> receiver.includeDirs(includeDirs)
            is LinkedHashSet<*> -> receiver.includeDirs(*includeDirs.toTypedArray())
            is IncludeDirectories -> receiver.includeDirs {
                includeDirs.applyTo(this)
            }

            else -> Unit
        }

        compilerOpts?.let(receiver::compilerOpts)
        linkerOpts?.let(receiver::linkerOpts)
        extraOpts?.let(receiver::extraOpts)
    }

    /**
     *  A collection of directories to look for headers.
     */
    @Serializable
    data class IncludeDirectories(
        /**
         * A collection of directories to search for headers.
         * It's the equivalent of the `-I<path>` compiler option.
         *
         * #### Usage example
         *
         * The following example demonstrates
         * how to add multiple directories containing header files in a `build.gradle.kts` file:
         *
         * ```kotlin
         * //build.gradle.kts
         * kotlin {
         *     linuxX64() {
         *         compilations.getByName("main") {
         *             cinterops {
         *                 val cinterop by creating {
         *                     defFile(project.file("custom.def"))
         *                     includeDirs {
         *                         allHeaders(project.file("src/main/headersDir1"), project.file("src/main/headersDir2"))
         *                     }
         *                 }
         *             }
         *         }
         *     }
         * }
         * ```
         *
         * In the example, the directories `src/main/headersDir1` and `src/main/headersDir2` in the project directory
         * are specified as locations containing the header files required for the cinterop process.
         *
         * @param includeDirs The directories to be included.
         */
        val allHeaders: Set<String>? = null,

        /**
         * Additional directories to search for headers listed in the `headers` property in the `.def` file.
         * It's equivalent to the `-headerFilterAdditionalSearchPrefix` cinterop tool option.
         *
         * #### Usage example
         *
         * The following example demonstrates how to add multiple directories containing header files in a `build.gradle.kts` file:
         *
         * ```kotlin
         * //build.gradle.kts
         * kotlin {
         *     linuxX64() {
         *         compilations.getByName("main") {
         *             cinterops {
         *                 val cinterop by creating {
         *                     defFile(project.file("custom.def"))
         *                     includeDirs {
         *                         headerFilterOnly(project.file("include/libs"))
         *                     }
         *                 }internal abstract class NamedKeyValueTransformingSerializer<T : BuildType<*>>(tSerializer: KSerializer<T>)
         *     : NamedKeyValueTransformingSerializer<T>(tSerializer)
         *             }
         *         }
         *     }
         * }
         * ```
         *
         * In the example, the directory `include/libs` is specified as the prefix for the directories listed in the `headers`
         * declared in the `custom.def`.
         *
         * @param includeDirs The directories to be included as prefixes for the header filters.
         */
        val headerFilterOnly: Set<String>? = null,
    ) {

        fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.CInteropSettings.IncludeDirectories) {
            allHeaders?.let(receiver::allHeaders)
            headerFilterOnly?.let(receiver::headerFilterOnly)
        }
    }

    private object IncludeDirContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
            when (element) {
                is JsonPrimitive -> String.serializer()
                is JsonArray -> SetSerializer(String.serializer())
                is JsonObject -> IncludeDirectories.serializer()
            }
    }
}
