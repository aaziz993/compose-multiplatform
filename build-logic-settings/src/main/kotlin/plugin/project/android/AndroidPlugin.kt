@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gradle.android
import gradle.decapitalized
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import plugin.project.kotlin.model.KotlinAndroidTarget
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinAndroidTarget } != false) {
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

    @Suppress("UnstableApiUsage")
    private fun Project.adjustAndroidSourceSets() {
        val variants = (if (projectProperties.type == ProjectType.APP)
            (android as BaseAppModuleExtension).applicationVariants
        else
            (android as LibraryExtension).libraryVariants) +

            (android as TestedExtension).let {
                it.testVariants + it.unitTestVariants
            }

        val buildTypes = (android as CommonExtension<*, *, *, *, *, *>).buildTypes.map(BuildType::getName)

        val flavours = android.flavorDimensionList.map { flavorDimension ->
            android.productFlavors
                .filter { productFlavor -> productFlavor.dimension == flavorDimension }
                .map(ProductFlavor::getName)
        }

        val isTextFixtures = (android as TestedExtension).testFixtures.enable

        println(
                """ANDROID
            BuildTypes: $buildTypes
            Flavours: $flavours
            IsTextFixtures: $isTextFixtures
        """.trimIndent(),
        )
//        listOf(
//            "main",
//            "test
//        )

        when (projectProperties.layout) {
            ProjectLayout.FLAT -> android.sourceSets.all {
                val isMain = name == SourceSet.MAIN_SOURCE_SET_NAME
                val buildType = android.buildTypes.find { name.contains(it.name, ignoreCase = true) }?.name
                val productFlavor = android.productFlavors.find { name.contains(it.name, ignoreCase = true) }?.name
                val isTest = name.contains("androidTest", ignoreCase = true) || name.contains("test", ignoreCase = true)

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
