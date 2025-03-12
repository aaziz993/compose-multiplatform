package gradle.plugins.sonar

import gradle.accessors.sonar
import org.gradle.api.Project

/**
 * Adds an action that configures SonarQube properties for the associated Gradle project.
 *
 *
 * *Global* SonarQube properties (e.g. server connection settings) have to be set on the "root" project of the SonarQube run.
 * This is the project that has the `sonar-gradle` plugin applied.
 *
 *
 * The action is passed an instance of `SonarQubeProperties`.
 * Evaluation of the action is deferred until `sonarqube.properties` is requested.
 * Hence it is safe to reference other Gradle model properties from inside the action.
 *
 *
 * SonarQube properties can also be set via system properties (and therefore from the command line).
 * This is mainly useful for global SonarQube properties like database credentials.
 * Every system property starting with `"sonar."` is automatically set on the "root" project of the SonarQube run
 * (i.e. the project that has the `sonar-gradle` plugin applied).
 * System properties take precedence over properties declared in build scripts.
 *
 * @param action an action that configures SonarQube properties for the associated Gradle project
 */
internal interface SonarExtension {

    /**
     * Defaults to `false`.
     * @return true if the project should be excluded from analysis.
     */
    val skipProject: Boolean?

    /**
     * Adds an action that configures SonarQube properties for the associated Gradle project.
     * <p>
     * <em>Global</em> SonarQube properties (e.g. server connection settings) have to be set on the "root" project of the SonarQube run.
     * This is the project that has the {@code sonar-gradle} plugin applied.
     * <p>
     * The action is passed an instance of {@code SonarQubeProperties}.
     * Evaluation of the action is deferred until {@code sonarqube.properties} is requested.
     * Hence it is safe to reference other Gradle model properties from inside the action.
     * <p>
     * SonarQube properties can also be set via system properties (and therefore from the command line).
     * This is mainly useful for global SonarQube properties like database credentials.
     * Every system property starting with {@code "sonar."} is automatically set on the "root" project of the SonarQube run
     * (i.e. the project that has the {@code sonar-gradle} plugin applied).
     * System properties take precedence over properties declared in build scripts.
     *
     * @param action an action that configures SonarQube properties for the associated Gradle project
     */
    val properties: Map<String, String>?

    /**
     * @return Name of the variant to analyze. If null we'll take the first release variant
     */
    val androidVariant: String?

    context(Project)
    fun applyTo() {
        skipProject?.let(sonar::setSkipProject)
        sonar.properties {
            property("sonar.projectVersion", version)
        }
        properties?.let { properties ->
            sonar.properties {
                properties.forEach { (key, value) -> property(key, value) }
            }
        }
        androidVariant?.let(sonar::setAndroidVariant)
    }
}
