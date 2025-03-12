@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.noarg

import gradle.noArg
import gradle.trySet
import org.gradle.api.Project

internal interface NoArgExtension {

    val myAnnotations: List<String>?
    val myPresets: List<String>?
    val invokeInitializers: Boolean?
    context(Project)
    fun applyTo(){
        myAnnotations?.let(noArg::annotations)
        myPresets?.let(noArg.myPresets::addAll)
        noArg::invokeInitializers trySet invokeInitializers
    }
}
