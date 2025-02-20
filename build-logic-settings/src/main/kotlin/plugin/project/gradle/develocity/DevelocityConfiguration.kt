package plugin.project.gradle.develocity

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import gradle.projectProperties
import gradle.isCI
import gradle.tryAssign
import java.util.*
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import org.gradle.kotlin.dsl.withType
import plugin.project.gradle.develocity.model.BuildScanConfiguration

@Suppress("UnstableApiUsage")
internal fun Settings.configureDevelocityConfiguration() =
    plugins.withType<DevelocityPlugin> {
        projectProperties.settings.gradle.develocity.let { develocity ->

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
                        develocity.localCache.let { localCache ->
                            local {
                                localCache.isEnabled?.let(::setEnabled)
                                localCache.isPush?.let(::setPush)
                                localCache.directory?.let(layout.rootDirectory::dir)?.let(::setDirectory)
                            }
                        }
                        develocity.remoteCache.let { remoteCache ->
                            remote(buildCache) {
                                remoteCache.isEnabled?.let(::setEnabled)

                                // Check access key presence to avoid build cache errors on PR builds when access key is not present
                                val accessKey = System.getenv().getOrElse("GRADLE_ENTERPRISE_ACCESS_KEY") {
                                    projectProperties.settings.gradle.gradleEnterpriseAccessKey
                                }
                                isPush = remoteCache.isPush == true && accessKey != null
                            }
                        }
                    }
                }
            }
        }
    }

context(Settings)
private fun com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration.configureFrom(
    config: BuildScanConfiguration
) {
    val startParameter = gradle.startParameter

    val scanJournal = layout.rootDirectory.file("scan-journal.log").asFile

    config.background?.let { background ->
        background {
            configureFrom(background)
        }
    }

    apply {

        config.tag?.let(::tag)
        config.values?.forEach { (name, value) -> value(name, value) }
        config.links?.forEach { (name, url) -> link(name, url) }

        buildScanPublished {
            scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
        }

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
                    username {
                        username[it] ?: username[""] ?: it
                    }
                }

                obfuscation.hostname?.let { hostname ->
                    hostname {
                        hostname[it] ?: hostname[""] ?: it
                    }
                }

                obfuscation.ipAddresses?.let { ipAddresses ->
                    ipAddresses {
                        it.map { ipAddresses[it.hostAddress] ?: ipAddresses[""] ?: it.hostAddress }
                    }
                }

                obfuscation.externalProcessName?.let { externalProcessName ->
                    externalProcessName {
                        externalProcessName[it] ?: externalProcessName[""] ?: it
                    }
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
}



