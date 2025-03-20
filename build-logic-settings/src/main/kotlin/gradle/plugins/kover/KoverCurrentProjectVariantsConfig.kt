package gradle.plugins.kover

import gradle.api.tryAssign
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
    val sources: KoverVariantSources? = null,
    val instrumentation: KoverProjectInstrumentation? = null,
) {

    fun applyTo(recipient: KoverCurrentProjectVariantsConfig) {
        sources?.let { sources ->
            project.sources {
                excludeJava tryAssign sources.excludeJava
                excludedSourceSets tryAssign sources.excludedSourceSets
            }
        }

        instrumentation?.let { instrumentation ->
            project.instrumentation {
                disabledForAll tryAssign instrumentation.disabledForAll
                disabledForTestTasks tryAssign instrumentation.disabledForTestTasks
                excludedClasses tryAssign instrumentation.excludedClasses
                includedClasses tryAssign instrumentation.includedClasses
            }
        }
    }
}
