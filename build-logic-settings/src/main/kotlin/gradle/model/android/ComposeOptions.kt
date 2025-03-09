package gradle.model.android

import com.android.build.api.dsl.ComposeOptions
import gradle.libs
import gradle.settings
import gradle.trySet
import gradle.version
import gradle.versions
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
    fun applyTo(options: ComposeOptions) {
        options::kotlinCompilerExtensionVersion trySet (kotlinCompilerExtensionVersion
            ?: settings.libs.versions.version("kotlin"))
    }
}
