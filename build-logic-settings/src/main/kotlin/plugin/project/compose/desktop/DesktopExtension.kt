package plugin.project.compose.desktop

import gradle.desktop
import gradle.projectProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension

internal fun Project.configureDesktopExtension() =
    extensions.configure<ComposeExtension> {
        projectProperties.compose.desktop.let { desktop ->
            desktop {
                desktop.applyTo(this)
            }
        }
    }
