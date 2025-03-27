package gradle.plugins.spotless.groovy

import com.diffplug.gradle.spotless.BaseGroovyExtension
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.ifTrue
import gradle.plugins.spotless.FormatExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal abstract class BaseGroovyExtension<T : BaseGroovyExtension> : FormatExtension<T>() {

    abstract val importOrder: LinkedHashSet<String>?

    abstract val importOrderFile: String?

    abstract val removeSemicolons: Boolean?

    abstract val greclipse: GrEclipseConfig?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        importOrder?.toTypedArray()?.let(receiver::importOrder)
        importOrderFile?.let(receiver::importOrderFile)
        removeSemicolons?.ifTrue(receiver::removeSemicolons)

        greclipse?.let { greclipse ->
            greclipse.applyTo(
                (greclipse.version?.resolveVersion() ?: project.settings.libs.versions.version("greclipse"))
                    ?.let(receiver::greclipse) ?: receiver.greclipse(),
            )
        }
    }

    @Serializable
    internal data class GrEclipseConfig(
        val version: String? = null,
        val configFiles: Set<String>? = null,
        val withP2Mirrors: Map<String, String>? = null
    ) {

        fun applyTo(receiver: BaseGroovyExtension.GrEclipseConfig) {
            configFiles?.toTypedArray()?.let(receiver::configFile)
            withP2Mirrors?.let(receiver::withP2Mirrors)
        }
    }
}
