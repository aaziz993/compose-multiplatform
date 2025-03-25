package gradle.plugins.android

import com.android.build.api.dsl.ComposeOptions
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Optional settings for the Compose feature.
 */
@Serializable
internal data class ComposeOptions(
    /**
     * Sets the version of the Kotlin Compiler extension for the project or null if using
     * the default one.
     */
    val kotlinCompilerExtensionVersion: String? = null,
) {

    context(Project)
    fun applyTo(receiver: ComposeOptions) {
        receiver::kotlinCompilerExtensionVersion trySet (kotlinCompilerExtensionVersion
            ?: project.settings.libs.versions.version("kotlin"))
    }
}
