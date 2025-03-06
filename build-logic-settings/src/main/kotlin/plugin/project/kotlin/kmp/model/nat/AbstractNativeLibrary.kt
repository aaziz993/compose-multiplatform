package plugin.project.kotlin.kmp.model.nat

import gradle.trySet
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?

    context(Project)
    override fun applyTo(binary: Named) {
        super.applyTo(binary)

        binary as AbstractNativeLibrary

        binary::transitiveExport trySet transitiveExport
    }
}
