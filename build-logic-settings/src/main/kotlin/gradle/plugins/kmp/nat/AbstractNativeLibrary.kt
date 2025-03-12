package gradle.plugins.kmp.nat

import gradle.trySet
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as AbstractNativeLibrary

        named::transitiveExport trySet transitiveExport
    }
}
