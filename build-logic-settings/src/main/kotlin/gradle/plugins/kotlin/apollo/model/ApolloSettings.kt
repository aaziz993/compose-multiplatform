package gradle.plugins.kotlin.apollo.model


import gradle.accessors.catalog.libs


import gradle.accessors.settings
import gradle.plugins.kotlin.apollo.AndroidVariantService
import gradle.plugins.kotlin.apollo.ApolloExtension
import gradle.plugins.kotlin.apollo.ApolloKspProcessor
import gradle.plugins.kotlin.apollo.KotlinSourceSetService
import gradle.plugins.kotlin.apollo.Service
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class ApolloSettings(
    override val generateSourcesDuringGradleSync: Boolean? = null,
    override val linkSqlite: Boolean? = null,
    override val processors: Set<ApolloKspProcessor>? = null,
    override val androidVariantServices: Set<AndroidVariantService>? = null,
    override val kotlinSourceSetServices: Set<KotlinSourceSetService>? = null,
    override val services: Set<Service>? = null,
    override val enabled: Boolean = true,
) : ApolloExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("apollo3").id) {
            super.applyTo()
        }
}
