package plugin.project.gradle.doctor.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JavaHome(
    /**
     * Ensure that we are using `JAVA_HOME` to build with this Gradle.
     */
    val ensureJavaHomeMatches: Boolean? = null,
    /**
     * Ensure we have `JAVA_HOME` set.
     */
    val ensureJavaHomeIsSet: Boolean? = null,
    /**
     * Fail on any `JAVA_HOME` issues.
     */
    val failOnError: Boolean? = null,
    /**
     * Extra message text, if any, to show with the Gradle Doctor message. This is useful if you have a wiki page or
     * other instructions that you want to link for developers on your team if they encounter an issue.
     */
    val extraMessage: String? = null,
)
