package gradle.plugins.sonar.tasks

import gradle.api.tasks.ConventionTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.collection.tryPutAll
import gradle.collection.trySet
import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.kotlin.dsl.withType
import org.sonarqube.gradle.SonarTask

/**
 * Analyses one or more projects with the [SonarQube Scanner](http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle).
 * Can be used with or without the `"sonar-gradle"` plugin.
 * If used together with the plugin, `properties` will be populated with defaults based on Gradle's object model and user-defined
 * values configured via [SonarExtension].
 * If used without the plugin, all properties have to be configured manually.
 * For more information on how to configure the SonarQube Scanner, and on which properties are available, see the
 * [SonarQube Scanner documentation](http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle).
 */
internal data class SonarTask(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
    /**
     * @return The String key/value pairs to be passed to the SonarQube Scanner.
     * `null` values are not permitted.
     */
    val sonarProperties: Map<String, String>? = null,
    val setSonarProperties: Map<String, String>? = null,
    /**
     * Sets the [LogLevel] to use during Scanner execution. All logged messages from the Scanner at this level or
     * greater will be printed at the [LogLevel.LIFECYCLE] level, which is the default level for Gradle tasks. This
     * can be used to specify the level of Sonar Scanner which it output during standard task execution, without needing
     * to override the log level for the full Gradle execution.
     *
     *
     * This overrides the default [LogOutput] functionality, which passes logs through to the Gradle logger without
     * modifying the log level.
     *
     * @param logLevel the minimum log level to include in [LogLevel.LIFECYCLE] logs
     */
    val useLoggerLevel: LogLevel? = null,
) : ConventionTask<SonarTask>() {

    context(Project)
    override fun applyTo(receiver: SonarTask) {
        super.applyTo(receiver)

        receiver.properties.get() tryPutAll sonarProperties
        receiver.properties.get() trySet setSonarProperties
        receiver::useLoggerLevel trySet useLoggerLevel
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<SonarTask>())
}
