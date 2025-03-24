package gradle.plugins.kmp.nat


import gradle.api.trySet
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

internal abstract class Framework : AbstractNativeLibrary {

    abstract val isStatic: Boolean?

        context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(named)

        named as Framework

        named::isStatic trySet isStatic
    }
}
