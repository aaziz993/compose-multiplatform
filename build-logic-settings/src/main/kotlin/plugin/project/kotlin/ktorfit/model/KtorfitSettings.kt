package plugin.project.kotlin.ktorfit.model

import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode
import gradle.id
import gradle.libs
import gradle.model.kotlin.ktorfit.KtorfitGradleConfiguration
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
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
        pluginManager.withPlugin(settings.libs.plugins.plugin("ktorfit").id) {
            super.applyTo()
        }
}
