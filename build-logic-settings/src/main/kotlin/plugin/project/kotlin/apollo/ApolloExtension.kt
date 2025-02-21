package plugin.project.kotlin.apollo

import com.apollographql.apollo3.gradle.internal.ApolloPlugin
import gradle.apollo
import gradle.libs
import gradle.moduleProperties
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureApolloExtension() =
    pluginManager.withPlugin(libs.plugins.apollo3.get().pluginId) {
        moduleProperties.settings.kotlin.apollo.let { apollo ->
            apollo {
                apollo.applyTo(this)
            }
        }
    }
