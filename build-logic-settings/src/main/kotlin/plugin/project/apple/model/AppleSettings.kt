package plugin.project.apple.model

import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Provider
import org.jetbrains.gradle.apple.AppleBuildDestination
import org.jetbrains.gradle.apple.AppleBuildSettings
import org.jetbrains.gradle.apple.AppleBuildTarget
import org.jetbrains.gradle.apple.AppleSourceSet
import org.jetbrains.gradle.apple.BuildConfiguration

@Serializable
internal data class AppleSettings(
    override val teamID: String? = null,
    val ios: AppleTarget? = null,
) : AppleProjectExtension
