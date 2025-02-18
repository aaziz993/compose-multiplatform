package plugin.apple

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

internal fun Project.configureIosKotlinNativeTarget(target: KotlinNativeTarget) =
    target.apply {
        binaries.framework {
            baseName = this@configureIosKotlinNativeTarget.name
            isStatic = true
            // Add it to avoid sqllite3 issues in iOS. Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
        }
    }

//public fun Project.registerAssembleXCFramework(): TaskProvider<Task> = tasks.register("assembleXCFramework") {
//    group = "build"
//    dependsOn(
////        "linkDebugFrameworkIosArm64",
////        "linkDebugFrameworkIosX64",
////        "linkDebugFrameworkIosSimulatorArm64",
//        "linkReleaseFrameworkIosArm64",
//        "linkReleaseFrameworkIosSimulatorArm64",
//    )
//
//    val xcFrameworkDir = layout.buildDirectory.dir("XCFrameworks/${rootProject.name}.xcframework")
//
//    doLast {
//        xcFrameworkDir.get().asFile.deleteRecursively()
//
//        exec {
//            commandLine(
//                "xcodebuild",
//                "-create-xcframework",
//
//                "-framework", "${layout.buildDirectory.get()}/bin/iosArm64/releaseFramework/${rootProject.name}.framework",
//                "-framework", "${layout.buildDirectory.get()}/bin/iosSimulatorArm64/releaseFramework/${rootProject.name}.framework",
//
////                "-framework", "${buildDir}/bin/iosArm64/debugFramework/shared.framework",
////                "-framework", "${buildDir}/bin/iosX64/debugFramework/shared.framework",
////                "-framework", "${buildDir}/bin/iosSimulatorArm64/debugFramework/shared.framework",
//
//                "-output", xcFrameworkDir.get().asFile.absolutePath,
//            )
//        }
//
//        println("XCFramework created at ${xcFrameworkDir.get().asFile.absolutePath}")
//    }
//}
