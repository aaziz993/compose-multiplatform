package gradle.plugins.kover.currentproject

import klib.data.type.reflection.tryApply
import kotlinx.kover.gradle.plugin.dsl.KoverCurrentProjectVariantsConfig
import kotlinx.serialization.Serializable

/**
 * Type for customizing report variants shared by the current project.
 *
 * A report variant is a set of information used to generate a reports, namely:
 * project classes, a list of Gradle test tasks, classes that need to be excluded from instrumentation.
 *
 * ```
 *  currentProject {
 *      // create report variant with custom name,
 *      // in which it is acceptable to add information from other variants of the current project, as well as `kover` dependencies
 *      createVariant("custom") {
 *          // ...
 *      }
 *
 *      // Configure the variant that is automatically created in the current project
 *      // For example, "jvm" for JVM target or "debug" for Android build variant
 *      providedVariant("jvm") {
 *          // ...
 *      }
 *
 *      // Configure the variant for all the code that is available in the current project.
 *      // This variant always exists for any type of project.
 *      totalVariant {
 *          // ...
 *      }
 *  }
 * ```
 */
@Serializable
internal data class KoverCurrentProjectVariantsConfig(
    override val sources: KoverVariantSources? = null,
    val createVariants: Set<CreateVariant>? = null,
    val copyVariants: Set<CopyVariant>? = null,
    val providedVariants: Set<ProvidedVariant>? = null,
    val totalVariant: KoverVariantConfigImpl? = null,
    val instrumentation: KoverProjectInstrumentation? = null,
) : KoverVariantConfig<KoverCurrentProjectVariantsConfig> {

    override fun applyTo(receiver: KoverCurrentProjectVariantsConfig) {
        super.applyTo(receiver)

        createVariants?.forEach { (variantName, config) ->
            receiver.createVariant(variantName, config::applyTo)
        }

        copyVariants?.forEach { (variantName, originalVariantName) ->
            receiver.copyVariant(variantName, originalVariantName)
        }

        providedVariants?.forEach { (variantName, config) ->
            receiver.providedVariant(variantName, config::applyTo)
        }

        receiver::totalVariant tryApply totalVariant?.let { totalVariant -> totalVariant::applyTo }
        instrumentation?.applyTo(receiver.instrumentation)
    }
}
