package plugin.project.kotlin.ktorfit

import de.jensklingenberg.ktorfit.gradle.KtorfitGradlePlugin
import gradle.moduleProperties
import gradle.ktorfit
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKtorfitGradleConfiguration() =
    plugins.withType<KtorfitGradlePlugin> {
        moduleProperties.settings.kotlin.ktorfit.let { ktorfit ->
            ktorfit {
                ::generateQualifiedTypeName trySet ktorfit.generateQualifiedTypeName
                ::errorCheckingMode trySet ktorfit.errorCheckingMode
            }
        }
    }
