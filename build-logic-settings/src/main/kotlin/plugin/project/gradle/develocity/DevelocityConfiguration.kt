package plugin.project.gradle.develocity

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import gradle.isCI
import gradle.projectProperties
import gradle.tryAssign
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity
import org.gradle.kotlin.dsl.withType

@Suppress("UnstableApiUsage")
internal fun Settings.configureDevelocityConfiguration() =
    plugins.withType<DevelocityPlugin> {
        projectProperties.settings.gradle.develocity.let { develocity ->

            develocity {
                develocity.buildScan?.let { buildScan ->
                    buildScan {
                        buildScan.applyTo(this)
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



