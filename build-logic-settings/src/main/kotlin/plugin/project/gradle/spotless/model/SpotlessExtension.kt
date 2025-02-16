package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.GradleProvisioner
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessExtensionPredeclare
import com.diffplug.spotless.LineEnding
import org.gradle.api.GradleException

internal interface SpotlessExtension {

    val lineEndings: LineEnding?
    val encoding: String?
    val ratchetFrom: String?
    val enforceCheck: Boolean?
    val predeclareDepsFromBuildscript: Boolean?
    val predeclareDeps: Boolean?
    val formats: Map<String, FormatSettings>?
    val kotlinGradle: KotlinGradleExtension?
}
