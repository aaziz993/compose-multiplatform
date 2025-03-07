package gradle.model.gradle.develocity

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
    fun applyTo(
        configuration: com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration
    ) {
        val startParameter = gradle.startParameter

        val scanJournal = layout.rootDirectory.file("scan-journal.log").asFile

        if (background != null) {
            configuration.background {
                background.applyTo(this)
            }
        }

        configuration.apply {

            tag?.let(configuration::tag)
            values?.forEach { (name, value) -> configuration.value(name, value) }
            links?.forEach { (name, url) -> configuration.link(name, url) }

            buildScanPublished {
                scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
            }

            configuration.termsOfUseUrl tryAssign termsOfUseUrl
            configuration.termsOfUseAgree tryAssign termsOfUseAgree
            configuration.uploadInBackground tryAssign uploadInBackground

            if (this@BuildScanConfiguration.publishing != null) {
                configuration.publishing {
                    onlyIf { this@BuildScanConfiguration.publishing.ifAuthenticated == true }
                }
            }
            if (this@BuildScanConfiguration.obfuscation != null) {
                configuration.obfuscation {
                    this@BuildScanConfiguration.obfuscation.username?.let { username ->
                        username {
                            username[it] ?: username[""] ?: it
                        }
                    }

                    this@BuildScanConfiguration.obfuscation.hostname?.let { hostname ->
                        hostname {
                            hostname[it] ?: hostname[""] ?: it
                        }
                    }

                    this@BuildScanConfiguration.obfuscation.ipAddresses?.let { ipAddresses ->
                        ipAddresses {
                            it.map { ipAddresses[it.hostAddress] ?: ipAddresses[""] ?: it.hostAddress }
                        }
                    }

                    this@BuildScanConfiguration.obfuscation.externalProcessName?.let { externalProcessName ->
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
}
