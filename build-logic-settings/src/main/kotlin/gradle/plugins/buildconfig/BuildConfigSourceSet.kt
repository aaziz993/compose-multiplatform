package gradle.plugins.buildconfig

import com.github.gmazzo.gradle.plugins.BuildConfigSourceSet
import gradle.api.applyTo
import gradle.api.tryAssign
import gradle.plugins.buildconfig.generator.BuildConfigGenerator
import gradle.plugins.buildconfig.generator.BuildConfigJavaGenerator
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGenerator
import gradle.plugins.buildconfig.tasks.BuildConfigTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigSourceSet(
    override val className: String? = null,
    override val packageName: String? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val name: String = "",
    val generator: BuildConfigGenerator<*>? = null,
    val generateTask: BuildConfigTask? = null,
    val useJavaOutput: Boolean? = null,
    val useJavaOutputDsl: BuildConfigJavaGenerator? = null,
    val useKotlinOutput: Boolean? = null,
    val useKotlinOutputDsl: BuildConfigKotlinGenerator? = null,
    /**
     * Creates a secondary build class with the given [className] in the same package
     */
    val forClass: Set<ForClass>? = null,
) : BuildConfigClassSpec<BuildConfigSourceSet> {

    context(Project)
    override fun applyTo(recipient: BuildConfigSourceSet) {
        super.applyTo(recipient)
        recipient.className tryAssign className
        recipient.packageName tryAssign packageName

        buildConfigFields?.forEach { buildConfigField ->
            buildConfigField.applyTo(recipient.buildConfigFields)
        }

        recipient.generator tryAssign generator?.toBuildConfigGenerator()

        useJavaOutput?.takeIf { it }?.run { recipient.useJavaOutput() }

        useJavaOutputDsl?.let { useJavaOutputDsl ->
            recipient.useJavaOutput(useJavaOutputDsl::applyTo)
        }

        useKotlinOutput?.takeIf { it }?.run { recipient.useKotlinOutput() }

        useKotlinOutputDsl?.let { useKotlinOutputDsl ->
            recipient.useKotlinOutput(useKotlinOutputDsl::applyTo)
        }

        forClass?.forEach { forClass ->
            recipient.forClass(forClass.packageName, forClass.className) {
                forClass.configureAction?.applyTo(this)
            }
        }
    }
}


