@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.noarg

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.noArg
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import org.gradle.api.Project

internal interface NoArgExtension {

    val myAnnotations: List<String>?
    val myPresets: List<String>?
    val invokeInitializers: Boolean?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("noarg").id) {
            myAnnotations?.let(noArg::annotations)
            myPresets?.let(noArg.myPresets::addAll)
            noArg::invokeInitializers trySet invokeInitializers
        }
}
