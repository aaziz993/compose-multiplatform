package plugin.project.kotlin.model.language.nat

import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?

    context(Project)
    fun applyTo(library: AbstractNativeLibrary) {
        super.applyTo(library)
        library::transitiveExport trySet transitiveExport
    }
}
