package gradle.plugins.spotless.groovy

import com.diffplug.gradle.spotless.BaseGroovyExtension
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.plugins.spotless.FormatExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BaseGroovyExtension : FormatExtension() {

    abstract val importOrder: List<String>?

    abstract val importOrderFile: String?

    abstract val removeSemicolons: Boolean?

    abstract val greclipse: GrEclipseConfig?

    @Serializable
    internal data class GrEclipseConfig(
        val version: String? = null,
        val configFiles: List<String>? = null,
        val withP2Mirrors: Map<String, String>? = null
    ) {

        fun applyTo(config: BaseGroovyExtension.GrEclipseConfig) {
            configFiles?.let { configFiles ->
                config.configFile(*configFiles.toTypedArray())
            }

            withP2Mirrors?.let(config::withP2Mirrors)
        }
    }

    context(Project)
    override fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        super.applyTo(extension)

        extension as BaseGroovyExtension

        importOrder?.let { importOrder ->
            extension.importOrder(*importOrder.toTypedArray())
        }

        importOrderFile?.let(extension::importOrderFile)
        removeSemicolons?.takeIf { it }?.run { extension.removeSemicolons() }

        greclipse?.let { greclipse ->
            greclipse.applyTo(
                (greclipse.version?.resolveVersion() ?: settings.libs.versions.version("greclipse"))
                    ?.let(extension::greclipse) ?: extension.greclipse(),
            )
        }
    }
}
