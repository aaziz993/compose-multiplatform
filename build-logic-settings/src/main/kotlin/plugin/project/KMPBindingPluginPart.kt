@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import app.cash.sqldelight.core.decapitalize
import gradle.all
import gradle.kotlin
import gradle.libs
import gradle.moduleProperties
import gradle.trySet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.kotlin.model.KotlinSettings

private fun LanguageSettingsBuilder.configureFromAmperSettings(settings: KotlinSettings) {
    ::languageVersion trySet settings.languageVersion
    ::apiVersion trySet settings.apiVersion
    ::progressiveMode trySet settings.progressiveMode
    settings.languageFeatures?.forEach(::enableLanguageFeature)
    settings.optIns?.forEach(::optIn)
}

/**
 * Plugin logic, bind to specific module, when multiple targets are available.
 */
internal class KMPBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (moduleProperties.targets.isEmpty()) {
                return@with
            }

            plugins.apply(libs.plugins.kotlin.multiplatform.get().pluginId)

            // Enable Default Kotlin Hierarchy.
            extraProperties.set("kotlin.mpp.applyDefaultHierarchyTemplate", "true")

            // IOS Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties.set("org.jetbrains.compose.experimental.uikit.enabled", "true")

            adjustSourceSets()

            // Workaround for KTIJ-27212, to get proper compiler arguments in the common code facet after import.
            // Apparently, configuring compiler arguments for metadata compilation is not sufficient.
            // This workaround doesn't fix intermediate source sets, though, only the most common fragment.
//        kotlinMPE.compilerOptions {
//            configureFrom(module.mostCommonFragment.settings)
//        }

//        val hasJUnit5 = module.fragments
//            .any { it.settings.junit == JUnitVersion.JUNIT5 }
//        val hasJUnit = module.fragments
//            .any { it.settings.junit != JUnitVersion.NONE }
//        if (hasJUnit) {
//            project.configurations.all { config ->
//                config.resolutionStrategy.capabilitiesResolution.withCapability("org.jetbrains.kotlin:kotlin-test-framework-impl") { capability ->
//                    val selected = if (hasJUnit5) {
//                        capability.candidates
//                            .filter { (it.id as? ModuleComponentIdentifier)?.module == "kotlin-test-junit5" }
//                            .firstOrNull { (it.id as? ModuleComponentIdentifier)?.version == UsedVersions.kotlinVersion }
//                            ?: error(
//                                "No kotlin test junit5 dependency variant on classpath. Existing: " +
//                                    capability.candidates.joinToString { it.variantName },
//                            )
//                    }
//                    else {
//                        capability.candidates
//                            .firstOrNull { (it.id as? ModuleComponentIdentifier)?.version == UsedVersions.kotlinVersion }
//                            ?: error(
//                                "No kotlin test junit dependency variant on classpath. Existing: " +
//                                    capability.candidates.joinToString { it.variantName },
//                            )
//                    }
//
//                    capability.select(selected)
//                    capability.because("Select junit impl, since it is embedded")
//                }
//            }
//        }
        }
    }

    /**
     * Set Amper specific directory layout.
     */
//    private fun adjustSourceSetDirectories() {
//        kotlinMPE.sourceSets.all { sourceSet ->
//            val fragment = sourceSet.amperFragment
//            when {
//                // Do GRADLE_JVM specific.
//                layout == Layout.GRADLE_JVM -> {
//                    if (sourceSet.name == "jvmMain") {
//                        replacePenultimatePaths(sourceSet.kotlin, sourceSet.resources, "main")
//                    }
//                    else if (sourceSet.name == "jvmTest") {
//                        replacePenultimatePaths(sourceSet.kotlin, sourceSet.resources, "test")
//                    }
//                }
//
//                // Do AMPER specific.
//                layout == Layout.AMPER && fragment != null -> {
//                    sourceSet.kotlin.tryAdd(fragment.src).tryRemove { it.endsWith("kotlin") }
//                    sourceSet.resources.tryAdd(fragment.resourcesPath).tryRemove { it.endsWith("resources") }
//                }
//
//                layout == Layout.AMPER && fragment == null -> {
//                    sourceSet.kotlin.setSrcDirs(emptyList<File>())
//                    sourceSet.resources.setSrcDirs(emptyList<File>())
//                }
//            }
//        }
//    }

    @OptIn(ExperimentalWasmDsl::class)
//    private fun initTargets() = with(KotlinAmperNamingConvention) {
//        module.artifactPlatforms.filter { it.isLeaf }.forEach {
//            val targetName = it.targetName
//            when (it) {
//                Platform.ANDROID -> kotlinMPE.androidTarget(targetName)
//                Platform.JVM -> kotlinMPE.jvm(targetName)
//                Platform.IOS_ARM64 -> kotlinMPE.iosArm64(targetName)
//                Platform.IOS_SIMULATOR_ARM64 -> kotlinMPE.iosSimulatorArm64(targetName)
//                Platform.IOS_X64 -> kotlinMPE.iosX64(targetName)
//                Platform.MACOS_X64 -> kotlinMPE.macosX64(targetName)
//                Platform.MACOS_ARM64 -> kotlinMPE.macosArm64(targetName)
//                Platform.LINUX_X64 -> kotlinMPE.linuxX64(targetName)
//                Platform.LINUX_ARM64 -> kotlinMPE.linuxArm64(targetName)
//                Platform.TVOS_ARM64 -> kotlinMPE.tvosArm64(targetName)
//                Platform.TVOS_X64 -> kotlinMPE.tvosX64(targetName)
//                Platform.TVOS_SIMULATOR_ARM64 -> kotlinMPE.tvosSimulatorArm64(targetName)
//                Platform.WATCHOS_ARM64 -> kotlinMPE.watchosArm64(targetName)
//                Platform.WATCHOS_ARM32 -> kotlinMPE.watchosArm32(targetName)
//                Platform.WATCHOS_DEVICE_ARM64 -> kotlinMPE.watchosDeviceArm64(targetName)
//                Platform.WATCHOS_SIMULATOR_ARM64 -> kotlinMPE.watchosSimulatorArm64(targetName)
//                Platform.MINGW_X64 -> kotlinMPE.mingwX64(targetName)
//                Platform.ANDROID_NATIVE_ARM32 -> kotlinMPE.androidNativeArm32(targetName)
//                Platform.ANDROID_NATIVE_ARM64 -> kotlinMPE.androidNativeArm64(targetName)
//                Platform.ANDROID_NATIVE_X64 -> kotlinMPE.androidNativeX64(targetName)
//                Platform.ANDROID_NATIVE_X86 -> kotlinMPE.androidNativeX86(targetName)
//
//                // These are not leaf platforms, thus - should not get here.
//                // Configure js and wasm js by plugins
//                Platform.JS, Platform.WASM,
//                Platform.ANDROID_NATIVE, Platform.MINGW, Platform.WATCHOS,
//                Platform.IOS, Platform.MACOS, Platform.TVOS, Platform.APPLE,
//                Platform.LINUX, Platform.NATIVE, Platform.COMMON -> Unit
//            }
//        }
//
//        // Skip tests binary creation for now.
//        module.leafFragments.forEach { fragment ->
//            val target = fragment.target ?: return@forEach
//            with(target) target@{
//                if (fragment.platform != Platform.ANDROID) {
//                    fragment.maybeCreateCompilation {
//                        if (this@target is KotlinNativeTarget)
//                            adjust(
//                                this@target,
//                                this as KotlinNativeCompilation,
//                                fragment,
//                            )
//                    }
//                }
//            }
//        }
//    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        // Apply aliases
        kotlin.applyDefaultHierarchyTemplate {
            common {
                moduleProperties.aliases.forEach { alias ->
                    group(alias.name) {
                        alias.group.forEach { targetName ->
                            when (targetName) {
                                "jvm" -> withJvm()
                                "android" -> withAndroidTarget()
                                "ios" -> group("ios") {
                                    withIos()
                                }

                                "iosArm64" -> withIosArm64()
                                "iosX64" -> withIosX64()
                                "iosSimulatorArm64" -> withIosSimulatorArm64()
                                "js" -> withJs()
                                "wasm" -> withWasmJs()
                                else -> group(targetName)
                            }
                        }
                    }
                }
            }
        }

        // Flatten source sets source directories
        kotlin.sourceSets.all { sourceSet ->
            val sourceSetNameParts = "^(.*?)(Main|Test|TestDebug)?$".toRegex().matchEntire(sourceSet.name)!!

            val (kotlinPrefixPart, resourcesPrefixPart) = sourceSetNameParts.groupValues[2].decapitalize().let {
                when (it) {
                    "main", "" -> "src" to "resources"
                    else -> it to "${it}Resources"
                }
            }
            val suffixPart = sourceSetNameParts.groupValues[1].let {
                if (it == "common") "" else "@$it"
            }
            sourceSet.kotlin.setSrcDirs(listOf("$kotlinPrefixPart$suffixPart"))
            sourceSet.resources.setSrcDirs(listOf("$resourcesPrefixPart$suffixPart"))
        }

//        amperModuleExtraProperties.dependencies?.forEach { dependency -> dependency.add() }
//        amperModuleExtraProperties.testDependencies?.forEach { dependency -> dependency.add() }
    }
}
