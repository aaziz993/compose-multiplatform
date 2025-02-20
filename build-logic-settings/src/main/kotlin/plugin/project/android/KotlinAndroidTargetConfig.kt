package plugin.project.android

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

//internal fun Project.configureKotlinAndroidTarget(target: KotlinAndroidTarget) =
//    target.apply {
//        with(settings.extension) {
////        publishLibraryVariants(BuildType.RELEASE.applicationIdSuffix)
//            @OptIn(ExperimentalKotlinGradlePluginApi::class)
//            instrumentedTestVariant {
//                sourceSetTree = KotlinSourceSetTree.test
//            }
//            compilerOptions {
//                jvmTarget = libs.versions.android.compileOptions.targetCompatibility.get().toInt().toJvmTarget(),
//            }
//        }
//    }

private fun Int.toJvmTarget(): JvmTarget =
    JvmTarget.valueOf("JVM_${if (this > 8) "$this" else "1_$this"}")

