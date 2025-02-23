package plugin.project.compose.desktop.model

import gradle.kotlin
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.JvmApplication

@Serializable
internal data class JvmApplication(
    val fromSourceSet: String? = null,
    val fromKotlinTarget: String? = null,
    val disableDefaultConfiguration: Boolean? = null,
    /**
     * Depend on tasks
     */
    val dependsOn: List<String>? = null,
    val fromFiles: List<String>? = null,
    val mainClass: String? = null,
    val mainJar: String? = null,
    val javaHome: String? = null,
    val args: List<String>? = null,
    val jvmArgs: List<String>? = null,
    val nativeDistributions: JvmApplicationDistributions? = null,
    val buildTypes: JvmApplicationBuildTypes? = null,
) {

    context(Project)
    fun applyTo(application: JvmApplication) {
//        fromSourceSet?.let(kotlin.sourceSets::getByName)?.let { fromSourceSet ->
//            application.from(fromSourceSet)
//        }
        fromKotlinTarget?.let(kotlin.targets::getByName)?.let(application::from)
        disableDefaultConfiguration?.takeIf { it }?.run { application.disableDefaultConfiguration() }
        dependsOn?.map(tasks::getByName)?.let { tasks -> application.dependsOn(*tasks.toTypedArray()) }
        fromFiles?.let { fromFiles -> application.fromFiles(*fromFiles.toTypedArray()) }
        application::mainClass trySet mainClass
        application::javaHome trySet javaHome

        nativeDistributions?.let { nativeDistributions ->
            application.nativeDistributions {
                nativeDistributions.applyTo(this)
            }
        }

        buildTypes?.let { buildTypes ->
            application.buildTypes {
                buildTypes.applyTo(this)
            }
        }
    }
}
