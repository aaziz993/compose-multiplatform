package plugin.project.compose.desktop

import gradle.asModuleName
import gradle.desktop
import gradle.projectProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

internal fun Project.configureDesktopExtension() =
    extensions.configure<ComposeExtension> {
        projectProperties.compose.desktop.let { desktop ->
            desktop {
                desktop.applyTo(this)
            }
        }
    }
