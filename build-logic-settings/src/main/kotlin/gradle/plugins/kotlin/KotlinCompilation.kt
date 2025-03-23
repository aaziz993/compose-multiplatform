package gradle.plugins.kotlin

import gradle.api.BaseNamed
import gradle.api.ProjectNamed
import gradle.api.getByNameOrAll
import gradle.api.trySet
import gradle.plugins.kmp.KotlinSourceSet
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.KSerializer
import org.gradle.api.Project

/**
 * # Kotlin compilation
 * Represents the configuration of a Kotlin Compiler invocation.
 * The [KotlinCompilation] API is designed to ensure the correct and consistent propagation of any compilation changes
 * to all underlying tasks and configurations.
 * Use the [KotlinCompilation] API instead of getting tasks, configurations,
 * and other related domain objects directly through the Gradle API.
 * For Native targets, [KotlinCompilation] also provides an API to configure cinterop.
 *
 * A [KotlinTarget] contains multiple [KotlinCompilations](KotlinCompilation).
 * By default, a [KotlinTarget] contains two [KotlinCompilations](KotlinCompilation) with the names "main" and "test".
 *
 * Here's an example of how to use a [KotlinCompilation] to configure compilation tasks for the JVM target:
 * ```kotlin
 * // build.gradle.kts
 * kotlin {
 *   jvm {
 *     compilations.all {
 *       compileTaskProvider {
 *         // Configure the compile task here
 *       }
 *     }
 *   }
 * }
 * ```
 *
 * ## Main compilation
 * The [KotlinCompilation] with the name "main" represents a Kotlin compiler invocation for the main sources of the [KotlinTarget].
 * The results of the main compilation are publishable
 * and exposed via the [KotlinTarget.apiElementsConfigurationName] consumable configuration.
 *
Here's an example of how to use consume the outputs of the main JVM Compilation for custom processing:
 *
 * ```kotlin
 * // build.gradle.kts
 * val jvmMainClasses = kotlin.jvm().compilations.getByName("main").output.classesDirs
 * tasks.register<Jar>("customJar") {
 *     from(jvmMainClasses)
 * }
 * ```
 *
 * ## Test compilation
 * The [KotlinCompilation] with the name "test" represents a Kotlin Compiler invocation for the test source sets.
 * The test compilation is implicitly [associated with](KotlinCompilation.associateWith) the main compilation.
 * See [KotlinCompilation.associatedCompilations] for more information.
 * This means that the test compilation sees all dependencies, internal and public declarations of the main compilation.
 *
 * ## Custom compilation
 * It is possible to create additional custom compilations for a [KotlinTarget]:
 *
 * ```kotlin
 * kotlin {
 *   jvm {
 *     compilations.create("customCompilation")
 *   }
 * }
 * ```
 *
 * Use a separate Gradle project instead of creating a custom compilation for an easier and safer setup.
 *
 * ## Metadata target compilation
 *
 * The Kotlin metadata target is a special [KotlinTarget] that manages compiler invocations that compile the code of shared source sets.
 * There are no "main" or "test" Kotlin Compilations for the Metadata Target.
 * Instead, there is a dedicated compilation for each shared source set.
 *
 */
internal interface KotlinCompilation<T : org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>
    : HasKotlinDependencies<T>, ProjectNamed<T> {

    /**
     * The name of the compilation.
     */
    val compilationName: String

    override val name: String
        get() = compilationName

    /**
     * The [KotlinSourceSet] by default associated with this compilation.
     */
    val defaultSourceSet: KotlinSourceSet?

    /**
     * A collection of file system locations for the artifacts of compilation dependencies.
     */
    val compileDependencyFiles: List<String>?

    /**
     * Represents the output of a Kotlin compilation.
     */
    val output: KotlinCompilationOutput?

    /**
     * A list of all compilations that were previously associated with this compilation using [associateWith].
     *
     * For exmaple, 'test' compilations return 'setOf(main)' by default.
     *
     * @since 1.9.20
     */
    val associatedCompilations: Set<String>?

    context(Project)
    override fun applyTo(receiver: T) {
        defaultSourceSet?.applyTo(receiver.defaultSourceSet)
        receiver::compileDependencyFiles trySet compileDependencyFiles?.toTypedArray()?.let(::files)
        output?.applyTo(receiver.output)
        associatedCompilations
            ?.flatMap(receiver.target.compilations::getByNameOrAll)
            ?.forEach(receiver::associateWith)
    }
}

internal abstract class KotlinCompilationTransformingSerializer<T : KotlinCompilation<*>>(
    tSerializer: KSerializer<T>
) : KeyTransformingSerializer<T>(
    tSerializer,
    "compilationName",
)
