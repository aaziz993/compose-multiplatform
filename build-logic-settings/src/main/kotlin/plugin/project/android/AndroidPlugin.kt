@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import app.cash.sqldelight.core.decapitalize
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gradle.all
import gradle.android
import gradle.decapitalized
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
import org.jetbrains.amper.gradle.android.AndroidBindingPluginPart
import plugin.project.kotlin.model.language.KotlinAndroidTarget
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinAndroidTarget } == true) {
                return@with
            }

            when (projectProperties.type) {
                ProjectType.APP -> plugins.apply(settings.libs.plugins.plugin("androidApplication").id)

                else -> plugins.apply(settings.libs.plugins.plugin("androidLibrary").id)
            }

//            configureBaseExtension()

            adjustAndroidSourceSets()
            applyGoogleServicesPlugin()
        }
    }

    private fun Project.adjustAndroidSourceSets() {
//        val variants = (if (projectProperties.type == ProjectType.APP)
//            (android as BaseAppModuleExtension).applicationVariants
//        else
//            (android as LibraryExtension).libraryVariants) +
//            (android as TestedExtension).let {
//                it.testVariants + it.unitTestVariants
//            }
//
//        variants.map {
//            it.name
//        }.let {
//            println("ANDROID VARIANTS: $it")
//        }

        when (projectProperties.layout) {

            ProjectLayout.FLAT -> android.sourceSets.all {
                val sourceSetNameParts = "^.*?(Main|Test|TestDebug)?$".toRegex().matchEntire(name)!!

                val (compilationPrefixPart, resourcesPrefixPart) = sourceSetNameParts.groupValues[1]
                    .decapitalized()
                    .let { compilationName ->
                        when (compilationName) {
                            "main", "" -> "src" to ""

                            else -> compilationName to compilationName
                        }
                    }

                kotlin.setSrcDirs(listOf("$compilationPrefixPart@android"))
                java.setSrcDirs(listOf("$compilationPrefixPart@android"))
                manifest.srcFile("$compilationPrefixPart@android/AndroidManifest.xml")
                resources.setSrcDirs(listOf("${resourcesPrefixPart}Resources@android".decapitalized()))
                res.setSrcDirs(listOf("${resourcesPrefixPart}Res@android".decapitalized()))
                assets.setSrcDirs(listOf("${resourcesPrefixPart}Assets@android".decapitalized()))
                shaders.setSrcDirs(listOf("${resourcesPrefixPart}Shaders@android".decapitalized()))
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
