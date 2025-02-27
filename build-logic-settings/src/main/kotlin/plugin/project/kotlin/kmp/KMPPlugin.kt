package plugin.project.kotlin.kmp

import gradle.all
import gradle.decapitalized
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.prefixIfNotEmpty
import gradle.projectProperties
import gradle.settings
import net.pearx.kasechange.universalWordSplitter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.util.prefixIfNot
import plugin.project.model.ProjectLayout

internal class KMPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.takeIf { it.targets?.isNotEmpty() == true }?.let { kotlin ->
                plugins.apply(settings.libs.plugins.plugin("kotlin.multiplatform").id)

                // Enable Default Kotlin Hierarchy.
                extraProperties.set("kotlin.mpp.applyDefaultHierarchyTemplate", "true")

                // IOS Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
                extraProperties.set("org.jetbrains.compose.experimental.uikit.enabled", "true")

                kotlin.applyTo()

                adjustSourceSets()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (projectProperties.layout) {
                ProjectLayout.FLAT -> {
                    val androidTargets = kotlin.targets.filterIsInstance<KotlinAndroidTarget>()
                    kotlin.sourceSets.all { sourceSet ->
                        var targetPart: String
                        var srcPrefixPart: String
                        var resourcesPrefixPart: String

                        val androidTarget = androidTargets.find { target -> sourceSet.name.startsWith(target.targetName) }

                        if (androidTarget != null) {
                            targetPart = "@${androidTarget.targetName}"

                            val restPart = sourceSet.name.removePrefix(androidTarget.targetName).decapitalized()

                            val mainVariantName = androidTarget.mainVariant.sourceSetTree.get().name
                            val unitTestVariantName = "unitTest"
                            val instrumentedVariantName = androidTarget.instrumentedTestVariant.sourceSetTree.get().name

                            val splitter = universalWordSplitter(true)

                            when {
                                restPart.startsWith(mainVariantName) -> {
                                    srcPrefixPart = "src"
                                    resourcesPrefixPart = "resources"
                                }

                                restPart.startsWith(unitTestVariantName) -> {
                                    srcPrefixPart = "${unitTestVariantName}${
                                        splitter.splitToWords(
                                            restPart.removePrefix(unitTestVariantName),
                                        ).joinToString("+").prefixIfNotEmpty("+")
                                    }"
                                    resourcesPrefixPart = "${srcPrefixPart}Resources"
                                }

                                restPart.startsWith(instrumentedVariantName) -> {
                                    srcPrefixPart = "${instrumentedVariantName}${
                                        splitter.splitToWords(
                                            restPart.removePrefix(instrumentedVariantName),
                                        ).joinToString("+").prefixIfNotEmpty("+")
                                    }"
                                    resourcesPrefixPart = "${srcPrefixPart}Resources"
                                }

                                else -> {
                                    srcPrefixPart = restPart
                                    resourcesPrefixPart = "${srcPrefixPart}Resources"
                                }
                            }
                        }
                        else {

                            val sourceSetNameParts = "(.*[a-z\\d])([A-Z]\\w+)$".toRegex().matchEntire(sourceSet.name)!!

                            targetPart = sourceSetNameParts.groupValues[1].let { targetName ->
                                if (targetName == "common") "" else "@$targetName"
                            }

                            val compilationName = sourceSetNameParts.groupValues[2].decapitalized()

                            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) {
                                srcPrefixPart = "src"
                                resourcesPrefixPart = "resources"
                            }
                            else {
                                srcPrefixPart = compilationName
                                resourcesPrefixPart = "${compilationName}Resources"
                            }
                        }
                        println(
                            """SOURCE SET NAME PARTS:
                            SrcPart: $srcPrefixPart
                            ResPart: $resourcesPrefixPart
                            TargetPart: $targetPart
                        """.trimMargin(),
                        )

                        sourceSet.kotlin.setSrcDirs(listOf("$srcPrefixPart$targetPart"))
                        sourceSet.resources.setSrcDirs(listOf("$resourcesPrefixPart$targetPart"))
                    }
                }

                else -> Unit
            }

            sourceSets.forEach { sourceSet ->
                projectProperties.kotlin.sourceSets?.get(sourceSet.name)?.applyTo(sourceSet)
            }
        }
    }
}
