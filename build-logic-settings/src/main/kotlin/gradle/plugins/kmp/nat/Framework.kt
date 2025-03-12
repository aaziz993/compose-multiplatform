package gradle.plugins.kmp.nat

import gradle.api.trySet
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

internal abstract class Framework: AbstractNativeLibrary {
   abstract val isStatic: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as Framework

        named::isStatic trySet  isStatic
    }
}
