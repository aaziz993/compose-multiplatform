package gradle.plugins.kmp.nat


import gradle.api.trySet
import org.gradle.api.Named
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?

        context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(named)

        named as AbstractNativeLibrary

        named::transitiveExport trySet transitiveExport
    }
}
