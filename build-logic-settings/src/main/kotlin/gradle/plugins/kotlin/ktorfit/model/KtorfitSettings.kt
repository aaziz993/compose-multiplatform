package gradle.plugins.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.ktorfit.KtorfitGradleConfiguration
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class KtorfitSettings(
    override val generateQualifiedTypeName: Boolean? = null,
    override val errorCheckingMode: ErrorCheckingMode? = null,
    override val enabled: Boolean = true
) : KtorfitGradleConfiguration, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("ktorfit").id) {
            super.applyTo()
        }
}
