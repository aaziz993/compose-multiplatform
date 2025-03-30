package gradle.plugins.kotlin.apollo

import gradle.accessors.apollo
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class ApolloExtension(
    val generateSourcesDuringGradleSync: Boolean? = null,
    val linkSqlite: Boolean? = null,
    val processors: Set<ApolloKspProcessor>? = null,
    val androidVariantServices: Set<AndroidVariantService>? = null,
    val kotlinSourceSetServices: Set<KotlinSourceSetService>? = null,
    val services: Set<Service>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("com.apollographql.apollo3") {
            processors?.forEach { (schema, service, packageName) ->
                project.apollo.apolloKspProcessor(project.file(schema), service, packageName)
            }

            androidVariantServices?.forEach { androidVariantService ->
                project.apollo.createAllAndroidVariantServices(androidVariantService.sourceFolder, androidVariantService.nameSuffix) {
                    androidVariantService.service?.applyTo(this)
                }
            }

            kotlinSourceSetServices?.forEach { kotlinSourceSetServices ->
                project.apollo.createAllKotlinSourceSetServices(kotlinSourceSetServices.sourceFolder, kotlinSourceSetServices.nameSuffix) {
                    kotlinSourceSetServices.service?.applyTo(this)
                }
            }

            services?.forEach { service ->
                project.apollo.service(service.name) {
                    service.applyTo(this)
                }
            }

            project.apollo.generateSourcesDuringGradleSync tryAssign generateSourcesDuringGradleSync
            project.apollo.linkSqlite tryAssign linkSqlite
        }
}
