package gradle.plugins.develocity.buildscan

import com.gradle.develocity.agent.gradle.scan.BuildScanDataObfuscationConfiguration
import gradle.api.CI
import kotlin.collections.map
import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanDataObfuscationConfiguration(
    val username: Map<String, String>? = null,
    val hostname: Map<String, String>? = null,
    val ipAddresses: Map<String, String>? = null,
    val externalProcessName: Map<String, String>? = null,
) {

    fun applyTo(recipient: BuildScanDataObfuscationConfiguration) {
        username?.let { username ->
            recipient.username {
                username[it] ?: username[""] ?: it
            }
        }

        hostname?.let { hostname ->
            recipient.hostname {
                CI ?: hostname[it] ?: hostname[""] ?: it
            }
        }

        ipAddresses?.let { ipAddresses ->
            recipient.ipAddresses {
                it.map { ipAddresses[it.hostAddress] ?: ipAddresses[""] ?: it.hostAddress }
            }
        }

        externalProcessName?.let { externalProcessName ->
            recipient.externalProcessName {
                externalProcessName[it] ?: externalProcessName[""] ?: it
            }
        }
    }
}
