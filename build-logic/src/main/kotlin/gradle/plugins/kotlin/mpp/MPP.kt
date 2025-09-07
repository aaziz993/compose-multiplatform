package gradle.plugins.kotlin.mpp

import gradle.api.configureEach
import gradle.api.file.replace
import klib.data.type.primitives.string.decapitalize
import net.pearx.kasechange.splitToWords
import org.gradle.api.tasks.SourceSet
import org.jetbrains.amper.frontend.schema.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

context(project: Project)
public fun KotlinMultiplatformExtension.flatten(
    targetDelimiter: String = "@",
    androidAllVariantsDelimiter: String = "+",
    androidVariantDelimiter: String = ""
): Unit {
    val androidTargets = targets.filterIsInstance<KotlinAndroidTarget>()

    sourceSets.configureEach { sourceSet ->
        var targetPart: String
        var srcPrefixPart: String
        var resourcesPrefixPart: String

        val androidTarget = androidTargets.find { target -> sourceSet.name.startsWith(target.targetName) }

        if (androidTarget != null) {
            targetPart = "@${androidTarget.targetName}"

            val restPart = sourceSet.name.removePrefix(androidTarget.targetName).decapitalized()

            val mainSourceSetNamePrefix = androidTarget.mainVariant.sourceSetTree.get().name

            val testSourceSetNamePrefixes = listOf(
                SourceSet.TEST_SOURCE_SET_NAME,
                "unitTest",
                androidTarget.instrumentedTestVariant.sourceSetTree.get().name,
            )

            if (restPart == mainSourceSetNamePrefix) {
                srcPrefixPart = "src"
                resourcesPrefixPart = ""
            } else {
                val prefix = testSourceSetNamePrefixes.find { prefix -> restPart.startsWith(prefix) }

                if (prefix == null) {
                    srcPrefixPart = restPart
                        .splitToWords()
                        .let { words ->
                            words.firstOrNull()
                                .orEmpty() +
                                    words.drop(1)
                                        .joinToString(androidVariantDelimiter)
                                        .addPrefixIfNotEmpty(androidAllVariantsDelimiter)
                        }
                    resourcesPrefixPart = srcPrefixPart
                } else {
                    srcPrefixPart =
                        "$prefix${
                            restPart
                                .removePrefix(prefix)
                                .splitToWords()
                                .joinToString(androidVariantDelimiter)
                                .addPrefixIfNotEmpty(androidAllVariantsDelimiter)
                        }"
                    resourcesPrefixPart = srcPrefixPart
                }
            }
        } else {

            val sourceSetNameParts = "(.*[a-z\\d])([A-Z]\\w+)$".toRegex().matchEntire(sourceSet.name)!!

            targetPart = sourceSetNameParts.groupValues[1].let { targetName ->
                if (targetName == "common") "" else "${targetDelimiter}$targetName"
            }

            val compilationName = sourceSetNameParts.groupValues[2].decapitalize()

            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) {
                srcPrefixPart = "src"
                resourcesPrefixPart = ""
            } else {
                srcPrefixPart = compilationName
                resourcesPrefixPart = compilationName
            }
        }

        sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPrefixPart$targetPart")
        sourceSet.resources.replace(
            "src/${sourceSet.name}/resources",
            "${resourcesPrefixPart}Resources$targetPart".decapitalize()
        )
//        sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir("${resourcesPrefixPart}ComposeResources$targetPart".decapitalize())
    }
}