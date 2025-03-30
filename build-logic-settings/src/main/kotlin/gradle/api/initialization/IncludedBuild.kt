package gradle.api.initialization

import org.gradle.api.initialization.IncludedBuild

internal interface IncludedBuild<T : IncludedBuild> {

    val name: String?

    fun applyTo(receiver: T)
}
