@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import app.cash.sqldelight.core.decapitalize
import gradle.all
import gradle.android
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.android == null) {
                return@with
            }

            when (projectProperties.type) {
                ProjectType.APP -> plugins.apply(settings.libs.plugins.plugin("androidApplication").id)

                else -> plugins.apply(settings.libs.plugins.plugin("androidLibrary").id)
            }

            projectProperties.kotlin.android!!.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.androidTarget(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.androidTarget {
                    target.applyTo(this)
                }
            }

//            configureBaseExtension()

//        adjustCompilations()
//        applySettings()
            adjustAndroidSourceSets()
            applyGoogleServicesPlugin()
        }
    }

    private fun Project.adjustAndroidSourceSets() {
        when (projectProperties.layout) {
            ProjectLayout.FLAT -> android.sourceSets.all {
                val sourceSetNameParts = "^.*?(Main|Test|TestDebug)?$".toRegex().matchEntire(name)!!

                val (compilationPrefixPart, resourcesPrefixPart) = sourceSetNameParts.groupValues[1]
                    .decapitalize()
                    .let { compilationName ->
                        when (compilationName) {
                            "main", "" -> "src" to ""

                            else -> compilationName to compilationName
                        }
                    }

                kotlin.setSrcDirs(listOf("$compilationPrefixPart@android"))
                java.setSrcDirs(listOf("$compilationPrefixPart@android"))
                manifest.srcFile("$compilationPrefixPart@android/AndroidManifest.xml")
                resources.setSrcDirs(listOf("${resourcesPrefixPart}Resources@android".decapitalize()))
                res.setSrcDirs(listOf("${resourcesPrefixPart}Res@android".decapitalize()))
                assets.setSrcDirs(listOf("${resourcesPrefixPart}Assets@android".decapitalize()))
                shaders.setSrcDirs(listOf("${resourcesPrefixPart}Shaders@android".decapitalize()))
            }

            else -> Unit
        }
    }

    private fun Project.applyGoogleServicesPlugin() {
        if (file("google-services.json").exists()) {
            plugins.apply(settings.libs.plugins.plugin("google.playServices").id)
        }
    }
}
