@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose

import gradle.api.all
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
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.commons.imaging.ImageFormats
import org.apache.commons.imaging.Imaging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.internal.utils.uppercaseFirstChar
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.getPreparedComposeResourcesDir
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

private val DENSITIES = mapOf(
    "ldpi" to 18,
    "mdpi" to 24,
    "hdpi" to 36,
    "xhdpi" to 48,
    "xxhdpi" to 72,
    "xxxhdpi" to 96,
)

private val ICNS_SIZES = listOf(16, 32, 64, 128, 256, 512, 1024)

private val ICO_SIZES = listOf(16, 24, 32, 48, 64, 128, 256)

private const val COMPOSE_MULTIPLATFORM_ICON_NAME = "compose-multiplatform"

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            project.pluginManager.withPlugin("org.jetbrains.compose") {
                adjustResources()
                adjustAssembleJvmResTask()
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

        adjustDesktopIcons(composeResourcesDir)
    }

    private fun Project.adjustDesktopIcons(composeResourcesDir: String) {
        val drawableDir = project.file(composeResourcesDir).resolve("drawable")
        drawableDir.mkdirs()

        val composeMultiplatformSvg = drawableDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME.svg")

        if (!composeMultiplatformSvg.exists()) return

        DENSITIES.forEach { (qualifier, size) ->
            val drawableQualifiedDir = drawableDir.resolve("drawable-$qualifier")
            drawableQualifiedDir.mkdirs()

            val linuxIconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-linux.png")
            val windowsIconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-windows.ico")
            val macosIconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME.icns")

            svgToPng(composeMultiplatformSvg, linuxIconFile, size)
            Imaging.writeImage(ImageIO.read(linuxIconFile), windowsIconFile, ImageFormats.ICO)
            linuxIconFile.copyTo(macosIconFile, overwrite = true)
        }

        val drawableQualifiedDir = drawableDir.resolve("drawable-${DENSITIES.keys.last()}")

        // jpackage only supports .png on Linux, .ico on Windows, .icns on Mac, so a developer must do a conversion (probably from a png) to a 3 different formats.
        // Also it seems that ico and icns need to contain an icon in multiple resolutions, so the conversion becomes a bit inconvenient.
        compose.desktop.application.nativeDistributions {
            macOS {
                if (!iconFile.isPresent) iconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-macos.icns")
            }
            linux {
                if (!iconFile.isPresent) iconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-linux.png")
            }
            windows {
                if (!iconFile.isPresent) iconFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-windows.ico")
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

    // Trick to make assemble jvm resources task to work.
    private fun Project.adjustAssembleJvmResTask() = kotlin.targets.withType<KotlinJvmTarget> { adjustAssembleResTask(this) }
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

            compilation.defaultSourceSet.resources.srcDir(allCompilationResources)
        }
    }
}
