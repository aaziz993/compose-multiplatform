package plugin.project.kotlin.apollo

import com.apollographql.apollo3.compiler.OperationIdGenerator
import com.apollographql.apollo3.compiler.PackageNameGenerator
import com.apollographql.apollo3.compiler.hooks.ApolloCompilerJavaHooks
import com.apollographql.apollo3.gradle.internal.ApolloPlugin
import gradle.moduleProperties
import gradle.apollo
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import plugin.project.kotlin.apollo.model.Service

internal fun Project.configureApolloExtension() =
    plugins.withType<ApolloPlugin> {
        moduleProperties.settings.kotlin.apollo.let { apollo ->
            apollo {
                apollo.processors?.forEach { (schema, service, packageName) ->
                    apolloKspProcessor(file(schema), service, packageName)
                }

                apollo.androidServices?.forEach { androidService ->
                    createAllAndroidVariantServices(androidService.sourceFolder, androidService.nameSuffix) {
                        androidService.service?.applyTo(this)
                    }
                }

                apollo.kotlinService?.forEach { kotlinService ->
                    createAllKotlinSourceSetServices(kotlinService.sourceFolder, kotlinService.nameSuffix) {
                        kotlinService.service?.applyTo(this)
                    }
                }

                apollo.services?.forEach { service ->
                    service(service.name) {
                        service.applyTo(this)
                    }
                }

                generateSourcesDuringGradleSync tryAssign apollo.generateSourcesDuringGradleSync
                linkSqlite tryAssign apollo.linkSqlite
            }
        }
    }
