package gradle.plugins.buildconfig

import com.github.gmazzo.gradle.plugins.BuildConfigSourceSet
import gradle.api.applyTo
import gradle.api.tryAssign
import gradle.plugins.buildconfig.generator.BuildConfigGenerator
import gradle.plugins.buildconfig.generator.BuildConfigJavaGenerator
import gradle.plugins.buildconfig.generator.BuildConfigJavaGeneratorSerializer
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGenerator
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGeneratorSerializer
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
    val useJavaOutput: @Serializable(with = BuildConfigJavaGeneratorSerializer::class) Any? = null,
    val useKotlinOutput: @Serializable(with = BuildConfigKotlinGeneratorSerializer::class) Any? = null,
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

        when (useJavaOutput) {
            is Boolean -> recipient.useJavaOutput()
            is BuildConfigJavaGenerator -> recipient.useJavaOutput(useJavaOutput::applyTo)
            else -> Unit
        }

        when (useKotlinOutput) {
            is Boolean -> recipient.useKotlinOutput()
            is BuildConfigKotlinGenerator -> recipient.useKotlinOutput(useKotlinOutput::applyTo)
            else -> Unit
        }

        forClass?.forEach { forClass ->
            recipient.forClass(forClass.packageName, forClass.className) {
                forClass.configureAction?.applyTo(this)
            }
        }
    }
}


