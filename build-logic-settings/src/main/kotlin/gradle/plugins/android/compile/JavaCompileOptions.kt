package gradle.plugins.android.compile

import com.android.build.api.dsl.JavaCompileOptions
import gradle.plugins.android.AnnotationProcessorOptions
import kotlinx.serialization.Serializable

/** DSL object for javaCompileOptions. */
@Serializable
internal data class JavaCompileOptions(
    /** Options for configuration the annotation processor. */
    val annotationProcessorOptions: AnnotationProcessorOptions? = null,
) {

    fun applyTo(receiver: JavaCompileOptions) {
        annotationProcessorOptions?.applyTo(receiver.annotationProcessorOptions)
    }
}
