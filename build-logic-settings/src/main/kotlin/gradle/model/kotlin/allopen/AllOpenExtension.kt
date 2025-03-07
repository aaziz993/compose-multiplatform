package gradle.model.kotlin.allopen

import gradle.allOpen
import org.gradle.api.Project

internal interface AllOpenExtension {

    val myAnnotations: List<String>?
    val myPresets: List<String>?

    context(Project)
    fun applyTo() {
        myAnnotations?.let(allOpen::annotations)
        myPresets?.forEach(allOpen::preset)
    }
}
