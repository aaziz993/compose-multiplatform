package gradle.plugins.kotlin

import gradle.accessors.files
import gradle.api.ProjectNamed
import gradle.api.getByNameOrAll
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import gradle.plugins.kotlin.tasks.KotlinCompilationTaskImpl
import gradle.api.artifacts.Dependency
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
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
    val compilationName: String?
        get() = name

    /**
     * The [KotlinSourceSet] by default associated with this compilation.
     */
    val defaultSourceSet: KotlinSourceSet?

    /**
     * A collection of file system locations for the artifacts of compilation dependencies.
     */
    val compileDependencyFiles: Set<String>?
    val setCompileDependencyFiles: Set<String>?

    /**
     * Represents the output of a Kotlin compilation.
     */
    val output: KotlinCompilationOutput?

    /**
     * Provides access to the compilation task for this compilation.
     */
    val compileTaskProvider: KotlinCompilationTask<out org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>, *>?

    /**
     * Associates the current KotlinCompilation with another KotlinCompilation.
     *
     * After this compilation will:
     * - use the output of the [other] compilation as compile & runtime dependency
     * - add all 'declared dependencies' present on [other] compilation
     * - see all internal declarations of [other] compilation
     */
    val associatedCompilations: Set<String>?

    context(Project)
    override fun applyTo(receiver: T) {
        super<HasKotlinDependencies>.applyTo(receiver)

        defaultSourceSet?.applyTo(receiver.defaultSourceSet)
        receiver::compileDependencyFiles tryPlus compileDependencyFiles?.let(project::files)
        receiver::compileDependencyFiles trySet setCompileDependencyFiles?.let(project::files)
        output?.applyTo(receiver.output)
        (compileTaskProvider as KotlinCompilationTask<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>, *>?)
            ?.applyTo(receiver.compileTaskProvider.get())
        associatedCompilations
            ?.flatMap(receiver.target.compilations::getByNameOrAll)
            ?.forEach(receiver::associateWith)
    }
}

@Serializable
internal data class KotlinCompilationImpl(
    override val name: String? = null,
    override val compileTaskProvider: KotlinCompilationTaskImpl? = null,
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: Set<Dependency>? = null,
) : KotlinCompilation<org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>
