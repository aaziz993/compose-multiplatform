@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.noarg

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.noArg
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAddAll
import gradle.api.trySet
import org.gradle.api.Project

internal interface NoArgExtension {

    val myAnnotations: List<String>?
    val myPresets: List<String>?
    val invokeInitializers: Boolean?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("noarg").id) {
            myAnnotations?.let(project.noArg::annotations)
            project.noArg.myPresets tryAddAll myPresets
            project.noArg::invokeInitializers trySet invokeInitializers
        }
}
