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

internal abstract class BaseGroovyExtension<T: BaseGroovyExtension> : FormatExtension<T>() {

    abstract val importOrder: LinkedHashSet<String>?

    abstract val importOrderFile: String?

    abstract val removeSemicolons: Boolean?

    abstract val greclipse: GrEclipseConfig?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        importOrder?.toTypedArray()?.let(recipient::importOrder)
        importOrderFile?.let(recipient::importOrderFile)
        removeSemicolons?.takeIf { it }?.run { recipient.removeSemicolons() }

        greclipse?.let { greclipse ->
            greclipse.applyTo(
                (greclipse.version?.resolveVersion() ?: settings.libs.versions.version("greclipse"))
                    ?.let(recipient::greclipse) ?: recipient.greclipse(),
            )
        }
    }

    @Serializable
    internal data class GrEclipseConfig(
        val version: String? = null,
        val configFiles: Set<String>? = null,
        val withP2Mirrors: Map<String, String>? = null
    ) {

        fun applyTo(recipient: BaseGroovyExtension.GrEclipseConfig) {
            configFiles?.toTypedArray()?.let(recipient::configFile)
            withP2Mirrors?.let(recipient::withP2Mirrors)
        }
    }
}
