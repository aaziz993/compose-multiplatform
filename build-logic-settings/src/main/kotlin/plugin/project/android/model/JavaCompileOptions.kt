package plugin.project.android.model

import com.android.build.api.dsl.JavaCompileOptions
import kotlinx.serialization.Serializable

/** DSL object for javaCompileOptions. */
@Serializable
internal data class JavaCompileOptions(
    /** Options for configuration the annotation processor. */
    val annotationProcessorOptions: AnnotationProcessorOptions? = null,
) {

    fun applyTo(options: JavaCompileOptions) {
        annotationProcessorOptions?.applyTo(options.annotationProcessorOptions)
    }
}
