package gradle.plugins.kotlin.apollo

import gradle.accessors.apollo
import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
import org.gradle.api.Project

internal interface ApolloExtension {

    val generateSourcesDuringGradleSync: Boolean?
    val linkSqlite: Boolean?
    val processors: Set<ApolloKspProcessor>?
    val androidVariantServices: Set<AndroidVariantService>?
    val kotlinSourceSetServices: Set<KotlinSourceSetService>?
    val services: Set<Service>?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("apollo").id) {
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
