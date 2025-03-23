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
        receiver: com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration
    ) {
        val startParameter = gradle.startParameter

        val scanJournal = layout.rootDirectory.file("scan-journal.log").asFile

        background?.let { background ->
            receiver.background {
                background.applyTo(this)
            }
        }


        tag?.let(receiver::tag)
        values?.forEach { (name, value) -> receiver.value(name, value) }
        links?.forEach { (name, url) -> receiver.link(name, url) }

        receiver.buildScanPublished {
            scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
        }

        receiver.termsOfUseUrl tryAssign termsOfUseUrl
        receiver.termsOfUseAgree tryAssign termsOfUseAgree
        receiver.uploadInBackground tryAssign uploadInBackground
        publishing.applyTo(receiver.publishing)
        obfuscation?.applyTo(receiver.obfuscation)
        capture?.applyTo(receiver.capture)
    }
}
