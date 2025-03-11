package gradle.model.kotlin.kmp.nat

import gradle.trySet
import kotlinx.serialization.Serializable
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
