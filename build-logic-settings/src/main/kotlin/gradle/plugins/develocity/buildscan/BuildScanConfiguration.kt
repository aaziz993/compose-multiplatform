package gradle.plugins.develocity.buildscan

import gradle.api.tryAssign
import gradle.plugins.develocity.buildscan.Link
import java.util.*
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

@Serializable
internal data class BuildScanConfiguration(
    val background: BuildScanConfiguration? = null,
    val tag: String? = null,
    val values: Map<String, String>? = null,
    val links: Set<Link>? = null,
    val termsOfUseUrl: String? = null,
    val termsOfUseAgree: String? = null,
    val uploadInBackground: Boolean? = null,
    val publishing: BuildScanPublishingConfiguration = BuildScanPublishingConfiguration(),
    val obfuscation: BuildScanDataObfuscationConfiguration? = null,
    val capture: BuildScanCaptureConfiguration?
) {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo(
        recipient: com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration
    ) {
        val startParameter = gradle.startParameter

        val scanJournal = layout.rootDirectory.file("scan-journal.log").asFile

        background?.let { background ->
            recipient.background {
                background.applyTo(this)
            }
        }


        tag?.let(recipient::tag)
        values?.forEach { (name, value) -> recipient.value(name, value) }
        links?.forEach { (name, url) -> recipient.link(name, url) }

        recipient.buildScanPublished {
            scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
        }

        recipient.termsOfUseUrl tryAssign termsOfUseUrl
        recipient.termsOfUseAgree tryAssign termsOfUseAgree
        recipient.uploadInBackground tryAssign uploadInBackground
        publishing.applyTo(recipient.publishing)
        obfuscation?.applyTo(recipient.obfuscation)
        capture?.applyTo(recipient.capture)
    }
}
