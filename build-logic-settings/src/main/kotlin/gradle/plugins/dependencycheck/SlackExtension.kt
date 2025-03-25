package gradle.plugins.dependencycheck

import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.SlackExtension

@Serializable
internal data class SlackExtension(
    val enabled: Boolean? = null,
    val webhookUrl: String? = null
) {

    fun applyTo(receiver: SlackExtension) {
        enabled?.let(receiver::setEnabled)
        webhookUrl?.let(receiver::setWebhookUrl)
    }
}
