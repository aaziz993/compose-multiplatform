package gradle.plugins.kmp

import gradle.api.ProjectNamed
import gradle.api.applyTo
import org.gradle.api.DomainObjectSet
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.HasBinaries

internal interface HasBinaries<T : HasBinaries<DomainObjectSet<B>>, B : Named> {

    val binaries: Set<ProjectNamed<B>>?

    context(Project)
    fun applyTo(receiver: T) {
        binaries?.forEach { binary ->

            binary.applyTo(receiver.binaries)
        }
    }
}
