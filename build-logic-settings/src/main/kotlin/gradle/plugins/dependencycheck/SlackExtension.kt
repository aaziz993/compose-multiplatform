package gradle.plugins.dependencycheck

import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.SlackExtension

@Serializable
internal data class SlackExtension(
    val enabled: Boolean? = null,
    val webhookUrl: String? = null
) {

    fun applyTo(receiver: SlackExtension) {
        receiver::setEnabled trySet enabled
        receiver::setWebhookUrl trySet webhookUrl
    }
}
