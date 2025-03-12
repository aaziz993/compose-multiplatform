package gradle.plugins.kmp.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

@Serializable
internal data class NodeJsRootExtension(
    val downloadBaseUrl: String? = null,
    val version: String? = null,
) {

    fun applyTo(extension: NodeJsRootExtension) {
        extension::downloadBaseUrl trySet downloadBaseUrl
        extension::version trySet version
    }
}
