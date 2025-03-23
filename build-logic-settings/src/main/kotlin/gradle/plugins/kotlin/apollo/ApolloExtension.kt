package gradle.plugins.kotlin.apollo

import gradle.accessors.apollo
import gradle.api.tryAssign
import org.gradle.api.Project

internal interface ApolloExtension {

    val generateSourcesDuringGradleSync: Boolean?
    val linkSqlite: Boolean?
    val processors: Set<ApolloKspProcessor>?
    val androidServices: Set<AndroidVariantService>?
    val kotlinService: Set<KotlinSourceSetService>?
    val services: Set<Service>?

    context(Project)
    fun applyTo() {
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
