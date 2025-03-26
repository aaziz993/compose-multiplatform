package gradle.plugins.buildconfig

import gradle.api.NamedKeyTransformingSerializer
import gradle.api.applyTo
import gradle.api.tryAssign
import gradle.plugins.buildconfig.generator.BuildConfigGenerator
import gradle.plugins.buildconfig.generator.BuildConfigJavaGenerator
import gradle.plugins.buildconfig.generator.BuildConfigJavaGeneratorContentPolymorphicSerializer
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGenerator
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGeneratorContentPolymorphicSerializer
import gradle.plugins.buildconfig.tasks.BuildConfigTask
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BuildConfigSourceSet(
    override val className: String? = null,
    override val packageName: String? = null,
    override val buildConfigFields: List<BuildConfigField>? = null,
    override val name: String? = null,
    val generator: BuildConfigGenerator<*>? = null,
    val generateTask: BuildConfigTask? = null,
    val useJavaOutput: @Serializable(with = BuildConfigJavaGeneratorContentPolymorphicSerializer::class) Any? = null,
    val useKotlinOutput: @Serializable(with = BuildConfigKotlinGeneratorContentPolymorphicSerializer::class) Any? = null,
    /**
     * Creates a secondary build class with the given [className] in the same package
     */
    val forClass: Set<ForClass>? = null,
) : BuildConfigClassSpec<com.github.gmazzo.gradle.plugins.BuildConfigSourceSet> {

    context(Project)
    override fun applyTo(receiver: com.github.gmazzo.gradle.plugins.BuildConfigSourceSet) {
        super.applyTo(receiver)
        receiver.className tryAssign className
        receiver.packageName tryAssign packageName

        buildConfigFields?.forEach { buildConfigField ->
            buildConfigField.applyTo(receiver.buildConfigFields)
        }

        receiver.generator tryAssign generator?.toBuildConfigGenerator()

        when (useJavaOutput) {
            is Boolean -> receiver.useJavaOutput()
            is BuildConfigJavaGenerator -> receiver.useJavaOutput(useJavaOutput::applyTo)
            else -> Unit
        }

        when (useKotlinOutput) {
            is Boolean -> receiver.useKotlinOutput()
            is BuildConfigKotlinGenerator -> receiver.useKotlinOutput(useKotlinOutput::applyTo)
            else -> Unit
        }

        forClass?.forEach { forClass ->
            receiver.forClass(forClass.packageName, forClass.className) {
                forClass.configureAction?.applyTo(this)
            }
        }
    }
}

internal object BuildConfigSourceSetTransformingSerializer : NamedKeyTransformingSerializer<BuildConfigSourceSet>(
    BuildConfigSourceSet.serializer(),
)
