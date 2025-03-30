package gradle.plugins.develocity.buildscan

import com.gradle.develocity.agent.gradle.scan.BuildScanDataObfuscationConfiguration

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildScanDataObfuscationConfiguration(
    val username: Map<String, String>? = null,
    val hostname: Map<String, String>? = null,
    val ipAddresses: Map<String, String>? = null,
    val externalProcessName: Map<String, String>? = null,
) {

    fun applyTo(receiver: BuildScanDataObfuscationConfiguration) {
        username?.let { username ->
            receiver.username {
                username[it] ?: username[""] ?: it
            }
        }

        hostname?.let { hostname ->
            receiver.hostname {
                CI ?: hostname[it] ?: hostname[""] ?: it
            }
        }

        ipAddresses?.let { ipAddresses ->
            receiver.ipAddresses {
                it.map { ipAddresses[it.hostAddress] ?: ipAddresses[""] ?: it.hostAddress }
            }
        }

        externalProcessName?.let { externalProcessName ->
            receiver.externalProcessName {
                externalProcessName[it] ?: externalProcessName[""] ?: it
            }
        }
    }
}
