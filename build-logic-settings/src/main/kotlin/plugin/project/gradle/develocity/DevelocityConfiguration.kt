package plugin.project.gradle.develocity

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import gradle.amperProjectExtraProperties
import gradle.isCI
import gradle.tryAssign
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import org.gradle.kotlin.dsl.withType
import plugin.project.gradle.develocity.model.BuildScanConfiguration

internal fun Settings.configureDevelocityConfiguration() =
    plugins.withType<DevelocityPlugin> {
        amperProjectExtraProperties.settings.develocity.let { develocity ->
            develocity {
                develocity.buildScan?.let { buildScan ->
                    buildScan {
                        configureFrom(buildScan)
                    }
                }

                server tryAssign develocity.server
                edgeDiscovery tryAssign develocity.edgeDiscovery
                projectId tryAssign develocity.projectId
                allowUntrustedServer tryAssign develocity.allowUntrustedServer
                accessKey tryAssign develocity.accessKey

                buildCache {

                    if (isCI) {
                        local {
                            isEnabled = providers.gradleProperty("develocity.build.cache.local.enable").get().toBoolean()
                        }
                    }

                    remote(buildCache) {
                        isEnabled = develocity.remoteBuildCache
                        // Check access key presence to avoid build cache errors on PR builds when access key is not present
                        val accessKey = System.getenv().getOrElse("GRADLE_ENTERPRISE_ACCESS_KEY") {
                            null
                        }
                        isPush = isCI && accessKey != null
                    }
                }
            }
        }
    }

private fun com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration.configureFrom(
    config: BuildScanConfiguration
) = apply {
    config.background?.let { background ->
        background {
            configureFrom(background)
        }
    }

    config.tag?.let(::tag)
    config.values?.forEach { (name, value) -> value(name, value) }
    config.links?.forEach { (name, url) -> link(name, url) }
    termsOfUseUrl tryAssign config.termsOfUseUrl
    termsOfUseAgree tryAssign config.termsOfUseAgree
    uploadInBackground tryAssign config.uploadInBackground

    config.publishing?.let { publishing ->
        publishing {
            publishing.onlyIfAuthenticated?.takeIf { it }.run {
                onlyIf { it.isAuthenticated }
            }
        }
    }
    config.obfuscation?.let { obfuscation ->
        obfuscation {
            obfuscation.username?.let { username ->
                username(username::get)
            }

            obfuscation.hostname?.let { hostname ->
                hostname(hostname::get)
            }

            obfuscation.ipAddresses?.let { ipAddresses ->
                ipAddresses {
                    it.map { ipAddresses[it.hostAddress] }
                }
            }

            obfuscation.externalProcessName?.let { externalProcessName ->
                externalProcessName(externalProcessName::get)
            }
        }
    }

    config.capture?.let { capture ->
        capture {
            fileFingerprints tryAssign capture.fileFingerprints
            buildLogging tryAssign capture.buildLogging
            testLogging tryAssign capture.testLogging
            resourceUsage tryAssign capture.resourceUsage
        }
    }
}



