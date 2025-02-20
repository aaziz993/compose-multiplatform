package plugin.project.kotlinnative

import gradle.kotlin
import gradle.moduleProperties
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import plugin.project.kotlinnative.model.Framework

internal fun Project.configureKotlinNativeTarget() =
    kotlin.targets.withType<KotlinNativeTarget> {
        moduleProperties.settings.native.let { native ->
            native.binaries?.let { binaries ->
                binaries {
                    binaries.framework?.let { framework ->
                        framework {
                            configureFrom(framework)
                        }
                    }
                }
            }
        }
    }

context(Project)
internal fun org.jetbrains.kotlin.gradle.plugin.mpp.Framework.configureFrom(framework: Framework) {
    baseName = framework.baseName ?: "$group.${
        project.name
            .replace("[-_]".toRegex(), ".")
    }.$targetName"


    ::transitiveExport trySet framework.transitiveExport
    ::debuggable trySet framework.debuggable
    ::optimized trySet framework.optimized
    framework.linkerOpts?.toMutableList()?.let(::linkerOpts)
    ::binaryOptions trySet framework.binaryOptions?.toMutableMap()
    ::freeCompilerArgs trySet framework.freeCompilerArgs
    ::outputDirectory trySet framework.optimized?.let(::file)
    outputDirectoryProperty tryAssign framework.outputDirectoryProperty?.let(layout.projectDirectory::dir)

    ::isStatic trySet framework.isStatic
}
