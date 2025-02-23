package plugin.project.android

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import gradle.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get

//internal fun Project.configureBaseExtension() = extensions.configure<BaseExtension> {
//    namespace = "$group.${project.name.replace("[-_]".toRegex(), ".")}"
//
//    compileSdkVersion = "android-${libs.versions.android.compile.sdk.get()}"
//
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    sourceSets["main"].res.srcDirs("src/androidMain/res")
//    sourceSets["main"].assets.srcDirs("src/androidMain/assets")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
//
//    val proguardFile = "proguard.pro"
//
//    val consumerProguardFile = "consumer-proguard.pro"
//
//    defaultConfig {
//        minSdk = libs.versions.android.min.sdk.get().toString().toInt()
//
//        targetSdk = libs.versions.android.target.sdk.get().toString().toInt()
//
//        versionCode = libs.versions.android.version.code.get().toString().toInt()
//
//        versionName = libs.versions.android.version.name.get().toString()
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//
//        manifestPlaceholders["appAuthRedirectScheme"] = "empty"
//
////        missingDimensionStrategy(
////            FlavorDimension.contentType.name,
////            BuildFlavor.demo.name,
////        )
//
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments["room.schemaLocation"] =
//                    "$projectDir/schemas"
//            }
//        }
//
//        consumerProguardFiles(consumerProguardFile)
//    }
//
//    buildTypes {
//        getByName("debug") {
//            isMinifyEnabled = true
//            debuggable(false)
//            proguardFiles(
//                // Includes the default ProGuard rules files that are packaged with
//                // the Android Gradle plugin. To learn more, go to the section about
//                // R8 configuration files.
//                // getDefaultProguardFile() is a simple helper method that fetches them out of build/intermediates/proguard-files.
//                // The Android Gradle Plugin (AGP) puts them there.
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//
//                // List additional ProGuard rules for the given build type here. By default,
//                // Android Studio creates and includes an empty rules file for you (located
//                // at the root directory of each module).
//
//                // Includes a local, custom Proguard rules file
//                "proguard-rules.pro",
//            )
//            testProguardFiles(
//                // The proguard files listed here are included in the
//                // test APK only.
//                "test-proguard-rules.pro",
//            )
//        }
//        getByName("release") {
//            isMinifyEnabled = false
//            debuggable(true)
//            proguardFile(proguardFile)
//        }
//    }
//
//
//
//    (this@configure as CommonExtension<*, *, *, *, *, *>).apply {
////        flavorDimensions += FlavorDimension.contentType.name
//
////        productFlavors {
////            BuildFlavor.values().forEach {
////                create(it.name) {
////                    dimension = it.dimension.name
////                    if (this is ApplicationExtension && this is ApplicationProductFlavor) {
////                        if (it.applicationIdSuffix != null) {
////                            this.applicationIdSuffix = it.applicationIdSuffix
////                        }
////                    }
////                }
////            }
////        }
//
//        packaging {
//            resources {
//                excludes += "/META-INF/{AL2.0,LGPL2.1}"
//            }
//        }
//
//        buildFeatures {
//            buildConfig = true
//            viewBinding = true
//        }
//
//        lint {
//            checkReleaseBuilds = false
//            abortOnError = false
//        }
//
//        dependencies {
//            add("lintChecks", libs.compose.lint.checks)
//        }
//    }
//
//    compileOptions {
//
//        sourceCompatibility = libs.versions.android.compileOptions.sourceCompatibility.get().let(JavaVersion::toVersion)
//        targetCompatibility = libs.versions.android.compileOptions.targetCompatibility.get().let(JavaVersion::toVersion)
//
//        isCoreLibraryDesugaringEnabled = true
//    }
//
//    @Suppress("UnstableApiUsage")
//    testOptions {
//        targetSdk = defaultConfig.targetSdk
//    }
//
//    dependencies {
//        coreLibraryDesugaring(libs.android.desugar.jdk.libs)
//        androidTestImplementation(libs.androidx.test.core)
//        androidTestImplementation(libs.androidx.test.runner)
//    }
//}
