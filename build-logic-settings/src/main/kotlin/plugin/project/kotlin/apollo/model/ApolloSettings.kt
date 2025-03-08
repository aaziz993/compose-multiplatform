package plugin.project.kotlin.apollo.model

import gradle.id
import gradle.libs
import gradle.model.kotlin.apollo.AndroidService
import gradle.model.kotlin.apollo.ApolloExtension
import gradle.model.kotlin.apollo.ApolloKspProcessor
import gradle.model.kotlin.apollo.KotlinService
import gradle.model.kotlin.apollo.Service
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class ApolloSettings(
    override val generateSourcesDuringGradleSync: Boolean? = null,
    override val linkSqlite: Boolean? = null,
    override val processors: List<ApolloKspProcessor>? = null,
    override val androidServices: List<AndroidService>? = null,
    override val kotlinService: List<KotlinService>? = null,
    override val services: List<Service>? = null,
    override val enabled: Boolean = true,
) : ApolloExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("apollo3").id) {
            super.applyTo()
        }
}
