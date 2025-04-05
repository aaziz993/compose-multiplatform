package gradle.plugins.spotless.groovy

import com.diffplug.gradle.spotless.BaseGroovyExtension
import gradle.accessors.settings
import gradle.api.libs
import klib.data.type.reflection.trySet
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

        receiver::importOrder trySet importOrder
        receiver::importOrderFile trySet importOrderFile
        receiver::removeSemicolons trySet removeSemicolons

        greclipse?.let { greclipse ->
            greclipse.applyTo(
                (greclipse.version?.resolveVersion() ?: project.settings.libs.versions["greclipse"])
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
            receiver::configFile trySet configFiles
            receiver::withP2Mirrors trySet withP2Mirrors
        }
    }
}
