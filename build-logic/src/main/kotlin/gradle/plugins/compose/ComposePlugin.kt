@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose

import gradle.api.all
import gradle.api.configure
import gradle.api.configureEach
import gradle.api.project.ProjectLayout
import gradle.api.project.compose
import gradle.api.project.desktop
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.resources
import gradle.api.project.sourceSetsToComposeResourcesDirs
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.imageio.ImageIO
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.uppercaseFirstChar
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.commons.imaging.ImageFormats
import org.apache.commons.imaging.Imaging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.KMP_RES_EXT
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.compose.resources.getPreparedComposeResourcesDir
import org.jetbrains.kotlin.gradle.ComposeKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.KotlinTargetResourcesPublication
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

private val DENSITIES = mapOf(
    "ldpi" to 18,
    "mdpi" to 24,
    "hdpi" to 36,
    "xhdpi" to 48,
    "xxhdpi" to 72,
    "xxxhdpi" to 96,
)

private val THEMES = listOf("", "light", "dark")

private const val COMPOSE_MULTIPLATFORM_ICON_NAME = "compose-multiplatform"

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            project.pluginManager.withPlugin("org.jetbrains.compose") {
                adjustResources()
                adjustAssembleResTask()
            }
        }
    }

    private fun Project.adjustResources() = project.pluginManager.withPlugin("org.jetbrains.compose") {
        var composeResourcesDir = "src/commonMain/composeResources"
        when (project.projectScript.layout) {
            is ProjectLayout.Flat -> {
                kotlin.sourceSets.forEach { sourceSet ->
                    compose.resources.customDirectory(
                        sourceSet.name,
                        project.provider { sourceSetsToComposeResourcesDirs[sourceSet]!! },
                    )
                }
                composeResourcesDir = "composeResources"
            }

            else -> Unit
        }

        adjustIcons(composeResourcesDir)
    }

    private fun Project.adjustIcons(dir: String) {
        val composeResourcesDir = project.file(dir)
        if (!composeResourcesDir.exists()) return

        THEMES.forEach { theme ->
            val themePart = theme.addPrefixIfNotEmpty("-")

            val composeMultiplatformSvg = composeResourcesDir.resolve("drawable$themePart/$COMPOSE_MULTIPLATFORM_ICON_NAME.svg")
            if (!composeMultiplatformSvg.exists()) return@forEach

            DENSITIES.forEach { (qualifier, size) ->
                val drawableQualifiedDir = composeResourcesDir.resolve("drawable-$qualifier$themePart")
                drawableQualifiedDir.mkdirs()

                val pngFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-png.png")
                val icoFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-ico.ico")
                val icnsFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-icns.icns")

                if (!pngFile.exists()) svgToPng(composeMultiplatformSvg, pngFile, size)
                if (!icoFile.exists())
                    Imaging.writeImage(ImageIO.read(pngFile), icoFile, ImageFormats.ICO)
                if (!icnsFile.exists()) pngFile.copyTo(icnsFile, overwrite = true)
            }
        }

        val drawableQualifiedDir = THEMES.firstNotNullOfOrNull { theme ->
            composeResourcesDir.resolve("drawable-${DENSITIES.keys.last()}${theme.addPrefixIfNotEmpty("-")}").takeIf(File::exists)
        } ?: return

        // jpackage only supports .png on Linux, .ico on Windows, .icns on Mac, so a developer must do a conversion (probably from a png) to a 3 different formats.
        // Also it seems that ico and icns need to contain an icon in multiple resolutions, so the conversion becomes a bit inconvenient.
        compose.desktop.application.nativeDistributions {
            macOS {
                if (!iconFile.isPresent) iconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-icns.icns")
            }
            linux {
                if (!iconFile.isPresent) iconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-png.png")
            }
            windows {
                if (!iconFile.isPresent) iconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-ico.ico")
            }
        }
    }

    private fun svgToPng(svgFile: File, pngFile: File, size: Int) {
        FileInputStream(svgFile).use { input ->
            val transcoder = PNGTranscoder().apply {
                addTranscodingHint(PNGTranscoder.KEY_WIDTH, size.toFloat())
                addTranscodingHint(PNGTranscoder.KEY_HEIGHT, size.toFloat())
            }
            val transcoderInput = TranscoderInput(input)
            FileOutputStream(pngFile).use { output ->
                transcoder.transcode(transcoderInput, TranscoderOutput(output))
            }
        }
    }

    // Trick to make assemble jvm and native resources task to work after source sets directories change.
    private fun Project.adjustAssembleResTask() = kotlin.targets
        .matching { target -> target is KotlinJvmTarget }
        .configureEach { target -> adjustAssembleResTask(target) }

    private fun Project.adjustAssembleResTask(target: KotlinTarget) {
        target.compilations.all { compilation ->
            val compilationResources = files(
                {
                    compilation.allKotlinSourceSets.map { sourceSet -> getPreparedComposeResourcesDir(sourceSet) }
                },
            )
            val assembleResTask = tasks.named(
                "assemble${target.targetName.uppercaseFirstChar()}${compilation.name.uppercaseFirstChar()}Resources",
                AssembleTargetResourcesTask::class.java,
            ) {
                resourceDirectories.setFrom(compilationResources)
            }

            val allCompilationResources = assembleResTask.flatMap { it.outputDirectory.asFile }

            if (
                target.platformType in platformsForSetupKmpResources
                && compilation.name == KotlinCompilation.MAIN_COMPILATION_NAME
            ) {
                configureKmpResources(compilation, allCompilationResources)
            }
            else {
                configureResourcesForCompilation(compilation, allCompilationResources)
            }
        }
    }

    private val platformsForSetupKmpResources = listOf(
        KotlinPlatformType.native, KotlinPlatformType.js, KotlinPlatformType.wasm,
    )

    @OptIn(ComposeKotlinGradlePluginApi::class)
    private fun Project.configureKmpResources(
        compilation: KotlinCompilation<*>,
        allCompilationResources: Provider<File>
    ) {
        require(compilation.platformType in platformsForSetupKmpResources)
        val kmpResources = extraProperties.get(KMP_RES_EXT) as KotlinTargetResourcesPublication

        //For Native/Js/Wasm main resources:
        // 1) we have to configure new Kotlin component publication
        // 2) we have to collect all transitive main resources

        //TODO temporary API misuse. will be changed on the KMP side
        //https://youtrack.jetbrains.com/issue/KT-70909
        val target = compilation.target
        val kmpEmptyPath = provider { File("") }
        val emptyDir = layout.buildDirectory.dir("$RES_GEN_DIR/emptyResourcesDir").map { it.asFile }
        logger.info("Configure KMP component publication for '${compilation.target.targetName}'")

        kmpResources.publishResourcesAsKotlinComponent(
            target,
            { kotlinSourceSet ->
                if (kotlinSourceSet == compilation.defaultSourceSet) {
                    KotlinTargetResourcesPublication.ResourceRoot(allCompilationResources, emptyList(), emptyList())
                }
                else {
                    KotlinTargetResourcesPublication.ResourceRoot(emptyDir, emptyList(), emptyList())
                }
            },
            kmpEmptyPath,
        )

        val allResources = kmpResources.resolveResources(target)
        logger.info("Collect resolved ${compilation.name} resources for '${compilation.target.targetName}'")
        configureResourcesForCompilation(compilation, allResources)
    }

    private fun Project.configureResourcesForCompilation(
        compilation: KotlinCompilation<*>,
        directoryWithAllResourcesForCompilation: Provider<File>
    ) {
        compilation.defaultSourceSet.resources.srcDir(directoryWithAllResourcesForCompilation)

        //JS packaging requires explicit dependency
        if (compilation is KotlinJsCompilation) {
            tasks.named(compilation.processResourcesTaskName).configure { processResourcesTask ->
                processResourcesTask.dependsOn(directoryWithAllResourcesForCompilation)
            }
        }
    }
}
