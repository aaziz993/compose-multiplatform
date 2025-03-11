package gradle.model.android

import com.android.build.api.dsl.AnnotationProcessorOptions
import gradle.model.CommandLineArgumentProvider
import kotlinx.serialization.Serializable

/** Options for configuring Java annotation processor. */
@Serializable
internal data class AnnotationProcessorOptions(
    /**
     * Specifies the annotation processor classes to run.
     *
     * By default, this property is empty and the plugin automatically discovers and runs annotation
     * processors that you add to the annotation processor classpath. To learn more about adding
     * annotation processor dependencies to your project, read
     * [Add annotation processors](https://d.android.com/studio/build/dependencies#annotation_processor).
     */
    val classNames: List<String>? = null,
    /**
     * Specifies arguments that represent primitive types for annotation processors.
     *
     * If one or more arguments represent files or directories, you must instead use
     * [compilerArgumentProviders].
     *
     * @see [compilerArgumentProviders]
     */
    val arguments: Map<String, String>? = null,
    /**
     * Specifies arguments for annotation processors that you want to pass to the Android plugin
     * using the [gradle.model.CommandLineArgumentProvider] class.
     *
     * The benefit of using this class is that it allows you or the annotation processor author to
     * improve the correctness and performance of incremental and cached clean builds by applying
     * [incremental build property type annotations](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:up_to_date_checks).
     *
     * To learn more about how to use this class to annotate arguments for annotation processors and
     * pass them to the Android plugin, read
     * [Pass arguments to annotation processors](https://developer.android.com/studio/build/dependencies#processor-arguments).
     */
    val compilerArgumentProviders: List<CommandLineArgumentProvider>? = null
) {

    fun applyTo(options: AnnotationProcessorOptions) {
        classNames?.let(options.classNames::addAll)
        arguments?.let(options.arguments::putAll)
        compilerArgumentProviders?.let(options.compilerArgumentProviders::addAll)
    }
}
