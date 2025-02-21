package plugin.project.kotlin.apollo.model

import gradle.tryAssign
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
    val enabled: Boolean = true,
) : ApolloExtension {

    context(Project)
    fun applyTo(extension: com.apollographql.apollo3.gradle.api.ApolloExtension) {
        processors?.forEach { (schema, service, packageName) ->
            extension.apolloKspProcessor(file(schema), service, packageName)
        }

        androidServices?.forEach { androidService ->
            extension.createAllAndroidVariantServices(androidService.sourceFolder, androidService.nameSuffix) {
                androidService.service?.applyTo(this)
            }
        }

        kotlinService?.forEach { kotlinService ->
            extension.createAllKotlinSourceSetServices(kotlinService.sourceFolder, kotlinService.nameSuffix) {
                kotlinService.service?.applyTo(this)
            }
        }

        services?.forEach { service ->
            extension.service(service.name) {
                service.applyTo(this)
            }
        }

        extension.generateSourcesDuringGradleSync tryAssign generateSourcesDuringGradleSync
        extension.linkSqlite tryAssign linkSqlite
    }
}
