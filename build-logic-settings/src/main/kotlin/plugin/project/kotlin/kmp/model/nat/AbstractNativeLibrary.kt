package plugin.project.kotlin.kmp.model.nat

import gradle.trySet
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary
import plugin.project.kotlin.model.configure

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?

    context(Project)
    override fun applyTo(binaries: NamedDomainObjectCollection<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>) {
        super.applyTo(binaries)

        binaries.configure {
            this as AbstractNativeLibrary

            ::transitiveExport trySet this@AbstractNativeLibrary.transitiveExport
        }
    }
}
