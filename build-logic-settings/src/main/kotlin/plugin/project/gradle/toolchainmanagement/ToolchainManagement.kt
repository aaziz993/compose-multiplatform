package plugin.project.gradle.toolchainmanagement

import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType
import org.gradle.toolchains.foojay.FoojayToolchainsPlugin

@Suppress("UnstableApiUsage")
internal fun Settings.configureToolchainManagement() =
    plugins.withType<FoojayToolchainsPlugin> {
        toolchainManagement {
        }
    }

