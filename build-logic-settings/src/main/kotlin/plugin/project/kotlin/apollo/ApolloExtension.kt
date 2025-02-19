package plugin.project.kotlin.apollo

import com.apollographql.apollo3.gradle.internal.ApolloPlugin
import gradle.moduleProperties
import gradle.apollo
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureApolloExtension() =
    plugins.withType<ApolloPlugin> {
        moduleProperties.settings.kotlin.apollo.let { apollo ->
            apollo {
                apollo.processors?.forEach { (schema, service, packageName) ->
                    apolloKspProcessor(file(schema), service, packageName)
                }

                createAllAndroidVariantServices("",""){
                    name=""
                    this.languageVersion
                }

                generateSourcesDuringGradleSync tryAssign apollo.generateSourcesDuringGradleSync
                linkSqlite tryAssign apollo.linkSqlite
            }
        }
    }
