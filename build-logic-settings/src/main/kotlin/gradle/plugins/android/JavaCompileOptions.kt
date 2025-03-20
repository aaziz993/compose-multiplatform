package gradle.plugins.android

import com.android.build.api.dsl.JavaCompileOptions
import kotlinx.serialization.Serializable

/** DSL object for javaCompileOptions. */
@Serializable
internal data class JavaCompileOptions(
    /** Options for configuration the annotation processor. */
    val annotationProcessorOptions: AnnotationProcessorOptions? = null,
) {

    fun applyTo(recipient: JavaCompileOptions) {
        annotationProcessorOptions?.applyTo(recipient.annotationProcessorOptions)
    }
}
