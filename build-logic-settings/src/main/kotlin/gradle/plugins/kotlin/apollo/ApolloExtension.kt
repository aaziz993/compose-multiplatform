package gradle.plugins.kotlin.apollo

import com.apollographql.apollo3.gradle.internal.ApolloConvertSchemaTask
import com.apollographql.apollo3.gradle.internal.ApolloDownloadSchemaTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateIrTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateKspProcessorTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSchemaTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesFromIrTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateUsedCoordinatesAndCheckFragmentsTask
import com.apollographql.apollo3.gradle.internal.ApolloPushSchemaTask
import com.apollographql.apollo3.gradle.internal.ApolloRegisterOperationsTask
import gradle.accessors.apollo
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
    fun applyTo() {
        processors?.forEach { (schema, service, packageName) ->
            apollo.apolloKspProcessor(file(schema), service, packageName)
        }

        androidVariantServices?.forEach { androidVariantService ->
            apollo.createAllAndroidVariantServices(androidVariantService.sourceFolder, androidVariantService.nameSuffix) {
                androidVariantService.service?.applyTo(this)
            }
        }

        kotlinSourceSetServices?.forEach { kotlinSourceSetServices ->
            apollo.createAllKotlinSourceSetServices(kotlinSourceSetServices.sourceFolder, kotlinSourceSetServices.nameSuffix) {
                kotlinSourceSetServices.service?.applyTo(this)
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
