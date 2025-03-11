package gradle.model.gradle.develocity

import gradle.CI
import gradle.GITHUB
import gradle.SPACE
import gradle.TEAMCITY
import gradle.tryAssign
import java.util.*
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal data class BuildScanConfiguration(
    val background: BuildScanConfiguration? = null,
    val tag: String? = null,
    val values: Map<String, String>? = null,
    val links: List<Link>? = null,
    val termsOfUseUrl: String? = null,
    val termsOfUseAgree: String? = null,
    val uploadInBackground: Boolean? = null,
    val publishing: BuildScanPublishingConfiguration? = null,
    val obfuscation: BuildScanDataObfuscationConfiguration? = null,
    val capture: BuildScanCaptureConfiguration?
) {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo(
        configuration: com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration
    ) {
        val startParameter = gradle.startParameter

        val scanJournal = layout.rootDirectory.file("scan-journal.log").asFile

        background?.let { background ->
            configuration.background {
                background.applyTo(this)
            }
        }


        tag?.let(configuration::tag)
        values?.forEach { (name, value) -> configuration.value(name, value) }
        links?.forEach { (name, url) -> configuration.link(name, url) }

        configuration.buildScanPublished {
            scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
        }

        configuration.termsOfUseUrl tryAssign termsOfUseUrl
        configuration.termsOfUseAgree tryAssign termsOfUseAgree
        configuration.uploadInBackground tryAssign uploadInBackground

        publishing?.let { publishing ->
            configuration.publishing {
                onlyIf { publishing.ifAuthenticated == true }
            }
        }

        obfuscation?.let { obfuscation ->
            configuration.obfuscation {
                obfuscation.username?.let { username ->
                    username {
                        username[it] ?: username[""] ?: it
                    }
                }

                obfuscation.hostname?.let { hostname ->
                    hostname {
                        when {
                            GITHUB -> "GitHub"
                            TEAMCITY -> "TeamCity"
                            SPACE -> "Space"
                            CI -> "CI"
                            else -> hostname[it] ?: hostname[""] ?: it
                        }
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

        capture?.let { capture ->
            configuration.capture {
                fileFingerprints tryAssign capture.fileFingerprints
                buildLogging tryAssign capture.buildLogging
                testLogging tryAssign capture.testLogging
                resourceUsage tryAssign capture.resourceUsage
            }
        }
    }
}
