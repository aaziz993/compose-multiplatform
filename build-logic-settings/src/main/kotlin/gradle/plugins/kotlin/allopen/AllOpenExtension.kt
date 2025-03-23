package gradle.plugins.kotlin.allopen

import gradle.accessors.allOpen
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import org.gradle.api.Project

internal interface AllOpenExtension {

    val myAnnotations: List<String>?
    val myPresets: List<String>?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("allopen").id) {
            myAnnotations?.let(allOpen::annotations)
            myPresets?.forEach(allOpen::preset)
        }
}
