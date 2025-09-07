package gradle.plugins.kotlin.mpp


import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectProperties
import gradle.api.project.sourceSetsToComposeResourcesDirs
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.lowercaseFirst
import net.pearx.kasechange.splitToWords
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.gradle.kotlin.dsl.getByType

public class MPPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustSourceSets()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (val layout = projectProperties.layout) {
                is ProjectLayout.Flat -> {
                    val androidTargets = kotlin.targets.filterIsInstance<KotlinAndroidTarget>()

                    kotlin.sourceSets.configureEach { sourceSet ->
                        var targetPart: String
                        var srcPrefixPart: String
                        var resourcesPrefixPart: String

                        val androidTarget =
                            androidTargets.filter { target -> sourceSet.name.startsWith(target.targetName) }
                                .maxByOrNull { target -> target.name.length }

                        if (androidTarget != null) {
                            targetPart = "${layout.targetDelimiter}${androidTarget.targetName}"

                            val restPart = sourceSet.name.removePrefix(androidTarget.targetName).lowercaseFirst()

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
                                val prefix = testSourceSetNamePrefixes.find(restPart::startsWith)

                                if (prefix == null) {
                                    srcPrefixPart = restPart
                                        .splitToWords()
                                        .let { words ->
                                            words.firstOrNull()
                                                .orEmpty() +
                                                    words.drop(1)
                                                        .joinToString(layout.androidVariantDelimiter)
                                                        .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                                        }
                                    resourcesPrefixPart = srcPrefixPart
                                } else {
                                    srcPrefixPart =
                                        "$prefix${
                                            restPart
                                                .removePrefix(prefix)
                                                .splitToWords()
                                                .joinToString(layout.androidVariantDelimiter)
                                                .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                                        }"
                                    resourcesPrefixPart = srcPrefixPart
                                }
                            }
                        } else {

                            val sourceSetNameParts =
                                "(.*[a-z\\d])([A-Z]\\w+)$".toRegex().matchEntire(sourceSet.name)!!.groupValues

                            targetPart = sourceSetNameParts[1].let { targetName ->
                                if (targetName == "common") "" else "${layout.targetDelimiter}$targetName"
                            }

                            val compilationName = sourceSetNameParts[2].lowercaseFirst()

                            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) {
                                srcPrefixPart = "src"
                                resourcesPrefixPart = ""
                            } else {
                                srcPrefixPart = compilationName
                                resourcesPrefixPart = compilationName
                            }
                        }

                        sourceSet.kotlin.replace(
                            "src/${sourceSet.name}/kotlin",
                            "$srcPrefixPart$targetPart"
                        )
                        sourceSet.resources.replace(
                            "src/${sourceSet.name}/resources",
                            "${resourcesPrefixPart}Resources$targetPart".lowercaseFirst()
                        )
                        sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir("${resourcesPrefixPart}ComposeResources$targetPart".lowercaseFirst())
                    }
                }

                else -> Unit
            }
        }
    }
}
