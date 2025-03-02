package plugin.project.apple.model

import gradle.apple
import gradle.moduleName
import org.gradle.api.Project

internal interface AppleProjectExtension {

    val teamID: String?

    context(Project)
    fun applyTo() {
        apple.teamID = teamID ?: moduleName
    }
}
