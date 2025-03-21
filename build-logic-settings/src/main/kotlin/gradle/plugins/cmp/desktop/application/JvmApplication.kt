package gradle.plugins.cmp.desktop.application

import gradle.accessors.kotlin
import gradle.accessors.sourceSets
import gradle.api.getByNameOrAll
import gradle.api.trySet
import gradle.collection.act
import gradle.plugins.cmp.desktop.application.buildtype.JvmApplicationBuildTypes
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
    val dependsOn: Set<String>? = null,
    val fromFiles: Set<String>? = null,
    val mainClass: String? = null,
    val mainJar: String? = null,
    val javaHome: String? = null,
    val args: List<String>? = null,
    val setArgs: List<String>? = null,
    val jvmArgs: List<String>? = null,
    val setJvmArgs: List<String>? = null,
    val nativeDistributions: JvmApplicationDistributions? = null,
    val buildTypes: JvmApplicationBuildTypes? = null,
) {

    context(Project)
    fun applyTo(recipient: JvmApplication) {
        fromSourceSet?.let(sourceSets::getByName)?.let(recipient::from)
        fromKotlinTarget?.let(kotlin.targets::getByName)?.let(recipient::from)
        disableDefaultConfiguration?.takeIf { it }?.run { recipient.disableDefaultConfiguration() }
        dependsOn?.flatMap(tasks::getByNameOrAll)?.toTypedArray()?.let(recipient::dependsOn)
        fromFiles?.toTypedArray()?.let(recipient::fromFiles)
        recipient::mainClass trySet mainClass
        recipient::javaHome trySet javaHome
        args?.let(recipient.args::addAll)
        setArgs?.act(recipient.args::clear)?.let(recipient.args::addAll)
        jvmArgs?.let(recipient.jvmArgs::addAll)
        setJvmArgs?.act(recipient.jvmArgs::clear)?.let(recipient.jvmArgs::addAll)
        nativeDistributions?.applyTo(recipient.nativeDistributions)
        buildTypes?.applyTo(recipient.buildTypes)
    }
}
