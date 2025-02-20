@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.api.Plugin
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains

internal class AndroidBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.ANDROID !in moduleProperties.targets) {
                return@with
            }

//                plugins.apply(libs.plugins.android.get())

            if (moduleProperties.application) {
                plugins.apply(libs.plugins.android.application.get().pluginId)
            }
            else {
                plugins.apply(libs.plugins.android.library.get().pluginId)
            }

            moduleProperties.targets
                .filter { target -> target.type.isDescendantOf(TargetType.ANDROID) }
                .forEach { target -> target.add() }

//        adjustCompilations()
//        applySettings()
//        adjustAndroidSourceSets()
//        applyGoogleServicesPlugin()
        }
    }

//    private fun adjustAndroidSourceSets() = with(AndroidAmperNamingConvention) {
//        val shouldAddAndroidRes = module.artifactPlatforms.size == 1 &&
//            module.artifactPlatforms.contains(Platform.ANDROID)
//
//        // Adjust that source sets whose matching kotlin source sets are created by us.
//        // Can be evaluated after project evaluation.
//        androidSourceSets?.all {
//            val fragment = amperFragment
//            when {
//                // Do AMPER specific.
//                layout == Layout.AMPER && fragment != null -> {
//                    kotlin.setSrcDirs(listOf(fragment.src))
//                    java.setSrcDirs(listOf(fragment.src))
//                    manifest.srcFile(fragment.src.resolve("AndroidManifest.xml"))
//
//                    if (!fragment.isTest && shouldAddAndroidRes) {
//                        assets.setSrcDirs(listOf(module.moduleDir.resolve("assets")))
//                        res.setSrcDirs(listOf(module.moduleDir.resolve("res")))
//                    }
//
//                    // Also add all resources from dependants.
//                    val collectedResources = mutableSetOf<File>()
//                    val queue = mutableListOf(fragment)
//                    while (queue.isNotEmpty()) {
//                        val next = queue.removeFirst()
//                        queue.addAll(next.refineDependencies)
//                        if (next.androidSourceSet == null) {
//                            collectedResources.add(next.resourcesPath.toFile())
//                        }
//                    }
//                    collectedResources.add(fragment.resourcesPath.toFile())
//                    resources.setSrcDirs(collectedResources)
//                }
//
//                layout == Layout.AMPER && fragment == null -> {
//                    listOf(
//                        kotlin,
//                        java,
//                        resources,
//                        assets,
//                        res,
//                    ).forEach { it.setSrcDirs(emptyList<File>()) }
//                }
//            }
//        }
//    }
//
//    private fun adjustCompilations() = with(KotlinAmperNamingConvention) {
//        leafPlatformFragments.forEach { fragment ->
//            project.afterEvaluate {
//                val androidTarget = fragment.target ?: return@afterEvaluate
//                val compilations = if (fragment.isTest) {
//                    androidTarget.compilations.matching {
//                        val lowercaseName = it.name.lowercase()
//                        // Collect only unit test, but ignore instrumented test, since they will be
//                        // supported by supplementary modules.
//                        lowercaseName.contains("test") && lowercaseName.contains("unit")
//                    }
//                }
//                else {
//                    androidTarget.compilations.matching { !it.name.lowercase().contains("test") }
//                }
//                compilations.configureEach {
//                    // TODO do we need this at all? It seems redundant with the settings done in the KMP binding plugin
//                    compileTaskProvider.configureCompilerOptions(fragment.settings)
//
//                    kotlinSourceSets.forEach { compilationSourceSet ->
//                        if (compilationSourceSet != fragment.kotlinSourceSet) {
//                            compilationSourceSet.doDependsOn(fragment)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun applySettings() {
//        val firstAndroidFragment = leafPlatformFragments.first()
//        firstAndroidFragment.settings.jvm.release?.let { release ->
//            androidPE?.compileOptions {
//                targetCompatibility(release.legacyNotation)
//                sourceCompatibility(release.legacyNotation)
//            }
////            project.tasks.withType(JavaCompile::class.java).configureEach {
////                options.release.set(release.releaseNumber)
////            }
//        }
//
//        val signing = firstAndroidFragment.settings.android.signing
//        if (signing.enabled) {
//            val propertiesFile = module.moduleDir / signing.propertiesFile
//            if (propertiesFile.exists()) {
//                val keystoreProperties = propertiesFile.readProperties()
//                androidPE?.apply {
//                    signingConfigs {
//                        create(SIGNING_CONFIG_NAME) {
//                            keystoreProperties.storeFile?.let { storeFile ->
//                                setStoreFile((module.moduleDir / storeFile).toFile())
//                            }
//                            keystoreProperties.storePassword?.let { storePassword ->
//                                setStorePassword(storePassword)
//                            }
//                            keystoreProperties.keyAlias?.let { keyAlias ->
//                                setKeyAlias(keyAlias)
//                            }
//                            keystoreProperties.keyPassword?.let { keyPassword ->
//                                setKeyPassword(keyPassword)
//                            }
//                        }
//                    }
//                }
//            }
//            else {
//                val path = propertiesFile.normalize().absolutePathString()
//                project.logger.warn("Properties file $path not found. Signing will not be configured")
//            }
//        }
//
//        leafPlatformFragments.forEach { fragment ->
//            val androidSettings = fragment.settings.android
//            androidPE?.apply {
//                compileSdkVersion(androidSettings.compileSdk.versionNumber)
//                defaultConfig.apply {
//                    if (!module.type.isLibrary()) applicationId = androidSettings.applicationId
//                    namespace = android.namespace ?: "${project.group}.${project.name.replace("[-_]".toRegex(), ".")}"
//                    minSdk = androidSettings.minSdk.versionNumber
//                    maxSdk = androidSettings.maxSdk?.versionNumber
//                    targetSdk = androidSettings.targetSdk.versionNumber
//                    versionCode = androidSettings.versionCode
//                    versionName = androidSettings.versionName
//                }
//
//                buildTypes {
//                    getByName("release") {
//                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//                        isDebuggable = false
//                        isMinifyEnabled = true
//                        if (module.type.isApplication()) {
//                            isShrinkResources = true
//                        }
//                        signingConfigs.findByName(SIGNING_CONFIG_NAME)?.let { signing ->
//                            signingConfig = signing
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun applyGoogleServicesPlugin() {
//        if ((module.moduleDir / "google-services.json").exists()) {
//            project.plugins.apply("com.google.gms.google-services")
//        }
//    }
}
