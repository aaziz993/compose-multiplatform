package gradle.plugins.sonar

import gradle.collection.SerializableAnyMap
import gradle.collection.act
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.sonarqube.gradle.SonarProperties

/**
 * The Sonar properties for the current Gradle project that are to be passed to the Scanner.
 *
 *
 * The `properties` map is already populated with the defaults provided by Gradle, and can be further manipulated as necessary.
 * Before passing them on to the Scanner, property values are converted to Strings as follows:
 *
 *  * `Iterable`s are recursively converted and joined into a comma-separated String.
 *  * All other values are converted to Strings by calling their `toString()` method.
 *
 */
@Serializable
internal data class SonarProperties(
    /**
     * @return The Sonar properties for the current Gradle project that are to be passed to the Sonar gradle.
     */
    val properties: SerializableAnyMap? = null,
    val setProperties: SerializableAnyMap? = null
) {

    context(Project)
    fun applyTo(receiver: SonarProperties) {
        properties?.let(receiver::properties)
        setProperties?.act(receiver.properties::clear)?.let(receiver::properties)
        receiver.property("sonar.projectVersion", project.version)
    }
}
