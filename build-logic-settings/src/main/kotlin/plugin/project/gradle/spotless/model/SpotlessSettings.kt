package plugin.project.gradle.spotless.model

import com.diffplug.spotless.LineEnding
import kotlinx.serialization.Serializable

@Serializable
internal data class SpotlessSettings(
    override val lineEndings: LineEnding? = null,
    override val encoding: String? = null,
    override val ratchetFrom: String? = null,
    override val enforceCheck: Boolean? = null,
    override val predeclareDepsFromBuildscript: Boolean? = null,
    override val predeclareDeps: Boolean? = null,
    override val formats: Map<String, FormatSettings>? = null,
    override val kotlinGradle: KotlinGradleExtension? = null,
    val enabled: Boolean = true
) : SpotlessExtension
