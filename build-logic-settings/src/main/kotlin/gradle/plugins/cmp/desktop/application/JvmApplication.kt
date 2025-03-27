package gradle.plugins.cmp.desktop.application

import gradle.accessors.kotlin
import gradle.accessors.sourceSets
import gradle.api.getByNameOrAll
import gradle.api.trySet
import gradle.act
import gradle.ifTrue
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
    fun applyTo(receiver: JvmApplication) {
        fromSourceSet?.let(sourceSets::getByName)?.let(receiver::from)
        fromKotlinTarget?.let(project.kotlin.targets::getByName)?.let(receiver::from)
        disableDefaultConfiguration?.ifTrue(receiver::disableDefaultConfiguration)
        dependsOn?.flatMap(tasks::getByNameOrAll)?.toTypedArray()?.let(receiver::dependsOn)
        fromFiles?.toTypedArray()?.let(receiver::fromFiles)
        receiver::mainClass trySet mainClass
        receiver::javaHome trySet javaHome
        args?.let(receiver.args::addAll)
        setArgs?.act(receiver.args::clear)?.let(receiver.args::addAll)
        jvmArgs?.let(receiver.jvmArgs::addAll)
        setJvmArgs?.act(receiver.jvmArgs::clear)?.let(receiver.jvmArgs::addAll)
        nativeDistributions?.applyTo(receiver.nativeDistributions)
        buildTypes?.applyTo(receiver.buildTypes)
    }
}
