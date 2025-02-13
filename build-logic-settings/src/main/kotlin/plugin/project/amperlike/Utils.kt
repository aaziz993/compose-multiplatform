package plugin.project.amperlike

import kotlin.io.path.exists
import org.jetbrains.amper.frontend.AmperModuleFileSource
import org.jetbrains.amper.frontend.MetaModulePart
import org.jetbrains.amper.frontend.Platform
import plugin.project.amperlike.model.AmperLikeModule

internal val AmperLikeModule.moduleDir
    get() = (source as? AmperModuleFileSource)?.moduleDir
        ?: error("Cannot get the moduleDir of module '$userReadableName' because it doesn't have a file-based source " +
            "(the source type is ${source::class.simpleName})")

internal val AmperLikeBindingPluginPart.layout
    get() = (module.parts.find<MetaModulePart>()
        ?: error("No mandatory MetaModulePart in the module ${module.userReadableName}"))
        .layout

internal operator fun AmperLikeModuleWrapper.contains(platform: Platform) =
    artifactPlatforms.contains(platform)

internal val AmperLikeBindingPluginPart.hasGradleScripts
    get() = module.hasGradleScripts

internal val AmperLikeModule.hasGradleScripts
    get() = moduleDir.run {
        resolve("build.gradle.kts").exists() || resolve("build.gradle").exists()
    }
