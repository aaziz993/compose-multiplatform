package plugin.project.kotlin.apollo.model

import gradle.apollo
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

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
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("apollo3").id) {
            processors?.forEach { (schema, service, packageName) ->
                apollo.apolloKspProcessor(file(schema), service, packageName)
            }

            androidServices?.forEach { androidService ->
                apollo.createAllAndroidVariantServices(androidService.sourceFolder, androidService.nameSuffix) {
                    androidService.service?.applyTo(this)
                }
            }

            kotlinService?.forEach { kotlinService ->
                apollo.createAllKotlinSourceSetServices(kotlinService.sourceFolder, kotlinService.nameSuffix) {
                    kotlinService.service?.applyTo(this)
                }
            }

            services?.forEach { service ->
                apollo.service(service.name) {
                    service.applyTo(this)
                }
            }

            apollo.generateSourcesDuringGradleSync tryAssign generateSourcesDuringGradleSync
            apollo.linkSqlite tryAssign linkSqlite
        }
}
