package plugins.kotlin.apollo.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.apollo.AndroidService
import gradle.plugins.kotlin.apollo.ApolloExtension
import gradle.plugins.kotlin.apollo.ApolloKspProcessor
import gradle.plugins.kotlin.apollo.KotlinService
import gradle.plugins.kotlin.apollo.Service
import gradle.project.EnabledSettings
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
