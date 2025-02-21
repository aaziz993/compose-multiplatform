package plugin.project.gradle.spotless

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.gradle.spotless.JavascriptExtension
import com.diffplug.gradle.spotless.JsonExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.gradle.spotless.TypescriptExtension
import com.diffplug.gradle.spotless.YamlExtension
import com.diffplug.spotless.generic.LicenseHeaderStep
import com.diffplug.spotless.kotlin.KotlinConstants
import com.diffplug.spotless.kotlin.KtfmtStep
import gradle.libs
import gradle.moduleProperties
import gradle.spotless
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import plugin.project.gradle.spotless.model.BaseKotlinExtension
import plugin.project.gradle.spotless.model.CleanthatJavaConfig
import plugin.project.gradle.spotless.model.FormatAnnotationsConfig
import plugin.project.gradle.spotless.model.FormatSettings
import plugin.project.gradle.spotless.model.GoogleJavaFormatConfig
import plugin.project.gradle.spotless.model.ImportOrderConfig
import plugin.project.gradle.spotless.model.KtlintConfig
import plugin.project.gradle.spotless.model.LicenseHeaderConfig

private const val LICENSE_HEADER_DIR = "../"

internal fun Project.configureSpotlessExtension() =
    pluginManager.withPlugin(libs.plugins.spotless.get().pluginId) {
        moduleProperties.settings.gradle.spotless.let { spotless ->
            spotless {
                spotless.applyTo(this)
            }
        }
    }



