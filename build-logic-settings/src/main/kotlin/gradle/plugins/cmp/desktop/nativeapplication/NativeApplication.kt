package gradle.plugins.cmp.desktop.nativeapplication

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.NativeApplication

@Serializable
internal data class NativeApplication(
    val distributions: NativeApplicationDistributions? = null,
) {

    context(Project)
    fun applyTo(recipient: NativeApplication) {
        distributions?.applyTo(recipient.distributions)
    }
}
