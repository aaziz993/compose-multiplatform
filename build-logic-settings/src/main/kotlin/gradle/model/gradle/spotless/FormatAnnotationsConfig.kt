package gradle.model.gradle.spotless

import com.diffplug.gradle.spotless.JavaExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class FormatAnnotationsConfig(
    val addedTypeAnnotations: List<String>? = null,
    val removedTypeAnnotations: List<String>? = null
){
    fun applyTo(annotations: JavaExtension.FormatAnnotationsConfig){
        addedTypeAnnotations?.forEach(annotations::addTypeAnnotation)
        removedTypeAnnotations?.forEach(annotations::removeTypeAnnotation)
    }
}
