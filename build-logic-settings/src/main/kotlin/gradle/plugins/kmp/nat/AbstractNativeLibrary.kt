package gradle.plugins.kmp.nat


import gradle.api.trySet
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?

        context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(named)

        named as AbstractNativeLibrary

        named::transitiveExport trySet transitiveExport
    }
}
