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
import java.nio.file.Files
import javax.imageio.ImageIO
import klib.data.type.pairBy
import klib.data.type.primitives.string.ifNotEmpty
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

private val THEMES = listOf("", "-light", "-dark")

private const val COMPOSE_MULTIPLATFORM_ICON_NAME = "compose-multiplatform"

private val DENSITIES = mapOf(
    "ldpi" to 18,
    "mdpi" to 24,
    "hdpi" to 36,
    "xhdpi" to 48,
    "xxhdpi" to 72,
    "xxxhdpi" to 96,
)

private val IOS_ICON_SIZES = listOf(20, 29, 40, 60, 76, 83, 1024)
private val IOS_SCALES = listOf(1, 2, 3)
private val IOS_THEMES = listOf("", "dark", "tinted")
private const val IOS_APPICONSET_DIR = "appleApp/iosApp/Assets.xcassets/AppIcon.appiconset"

private val TVOS_ICON_SIZES = listOf(400, 1280)
private val TVOS_SCALES = listOf(1, 2, 1)
private const val TVOS_APPICONSET_DIR = "appleApp/TVosApp/Assets.xcassets/AppIcon.appiconset"

private val MACOS_ICNS_SIZES = setOf(16, 32, 64, 128, 256, 512, 1024)

private val WATCHOS_ICON_SIZES = listOf(48, 55, 58, 80, 87, 88, 172)
private const val WATCHOS_APPICONSET_DIR = "appleApp/WatchosApp Watch App/Assets.xcassets/AppIcon.appiconset"

private val MACOS_ICON_SIZES = listOf(16, 32, 64, 128, 256, 512, 1024)

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
        var resourcesDir = "src/commonMain/composeResources"
        when (project.projectScript.layout) {
            is ProjectLayout.Flat -> {
                kotlin.sourceSets.forEach { sourceSet ->
                    compose.resources.customDirectory(
                        sourceSet.name,
                        project.provider { sourceSetsToComposeResourcesDirs[sourceSet]!! },
                    )
                }
                resourcesDir = "composeResources"
            }

            else -> Unit
        }

        val composeResourcesDir = project.file(resourcesDir)
        if (composeResourcesDir.exists()) {
            adjustDesktopIcons(composeResourcesDir)
            adjustAppleIcons(composeResourcesDir)
        }
    }

    private fun Project.adjustDesktopIcons(composeResourcesDir: File) {
        THEMES.forEachIndexed { index, theme ->

            val svg = composeResourcesDir.resolve("drawable$theme/$COMPOSE_MULTIPLATFORM_ICON_NAME.svg")
            if (!svg.exists()) return@forEachIndexed

            DENSITIES.entries.forEachIndexed { index, (qualifier, size) ->
                val drawableQualifiedDir = composeResourcesDir.resolve("drawable-$qualifier$theme")
                drawableQualifiedDir.mkdirs()

                val pngFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-png.png")
                val icoFile = drawableQualifiedDir.resolve("$COMPOSE_MULTIPLATFORM_ICON_NAME-ico.ico")

                svgToPng(svg, pngFile, size)
                Imaging.writeImage(ImageIO.read(pngFile), icoFile, ImageFormats.ICO)
            }

            val icnsFile = composeResourcesDir.resolve("drawable$theme/$COMPOSE_MULTIPLATFORM_ICON_NAME-icns.icns")

            val icnsTempDir = Files.createTempDirectory("icns$theme-iconset").toFile()
            val pngs = MACOS_ICNS_SIZES.map { size ->
                icnsTempDir.resolve("icon_${size}x${size}.png").apply {
                    svgToPng(svg, this, size)
                }
            }

            Imaging.writeImage(ImageIO.read(pngs[3]), icnsFile, ImageFormats.ICNS)

            icnsTempDir.deleteRecursively()
        }

        val drawableQualifiedDir = THEMES.firstNotNullOfOrNull { theme ->
            composeResourcesDir.resolve("drawable-${DENSITIES.keys.last()}$theme").takeIf(File::exists)
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

    private fun Project.adjustAppleIcons(composeResourcesDir: File) {
        adjustIosIcons(composeResourcesDir)
        adjustTVosIcons(composeResourcesDir)
        adjustWatchosWatchAppIcons(composeResourcesDir)
    }

    private fun Project.adjustIosIcons(composeResourcesDir: File) {
        val appIconSet = rootProject.file(IOS_APPICONSET_DIR).apply(File::mkdirs)
        val images = mutableListOf<Map<String, Any>>()

        THEMES.forEachIndexed { index, theme ->
            val svg = composeResourcesDir.resolve("drawable$theme/$COMPOSE_MULTIPLATFORM_ICON_NAME.svg")
            if (!svg.exists()) return@forEachIndexed
            val iosTheme = IOS_THEMES[index]
            val iosThemePart = iosTheme.ifNotEmpty { "-$it" }

            IOS_ICON_SIZES.forEach { size ->
                IOS_SCALES.forEach { scale ->
                    val filename = "icon-${size}x$size${if (scale > 1) "@${scale}x" else ""}$iosThemePart.png"
                    val file = appIconSet.resolve(filename)
                    file.parentFile.mkdirs()
                    svgToPng(svg, file, size * scale)
                    images.add(
                        listOfNotNull(
                            "size" to "${size}x$size",
                            "idiom" to "iphone",
                            "filename" to filename,
                            "scale" to "${scale}x",
                            iosTheme.takeIf(String::isNotEmpty)?.pairBy("appearance"),
                        ).toMap(),
                    )
                }
            }
        }

        writeContentsJson(appIconSet, images)
    }

    private fun Project.adjustTVosIcons(composeResourcesDir: File) {
        val appIconSet = rootProject.file(TVOS_APPICONSET_DIR).apply(File::mkdirs)
        val images = mutableListOf<Map<String, Any>>()
        val svg = composeResourcesDir.resolve("drawable/$COMPOSE_MULTIPLATFORM_ICON_NAME.svg")
        if (!svg.exists()) return

        TVOS_ICON_SIZES.forEachIndexed { index, size ->
            val scale = TVOS_SCALES.getOrElse(index) { 1 }
            val filename = "icon-${size}x$size${if (scale > 1) "@${scale}x" else ""}.png"
            val file = appIconSet.resolve(filename)
            svgToPng(svg, file, size * scale)
            images.add(
                mapOf(
                    "size" to "${size}x$size",
                    "idiom" to "tv",
                    "filename" to filename,
                    "scale" to "${scale}x",
                ),
            )
        }

        writeContentsJson(appIconSet, images)
    }

    private fun Project.adjustWatchosWatchAppIcons(composeResourcesDir: File) {
        val appIconSet = rootProject.file(WATCHOS_APPICONSET_DIR).apply { mkdirs() }
        val images = mutableListOf<Map<String, Any>>()
        val svg = composeResourcesDir.resolve("drawable/$COMPOSE_MULTIPLATFORM_ICON_NAME.svg")
        if (!svg.exists()) return

        WATCHOS_ICON_SIZES.forEach { size ->
            val filename = "icon-${size}x$size.png"
            val file = appIconSet.resolve(filename)
            svgToPng(svg, file, size * 2) // watchOS always @2x
            images.add(
                mapOf(
                    "size" to "${size}x$size",
                    "idiom" to "watch",
                    "filename" to filename,
                    "scale" to "2x",
                ),
            )
        }

        writeContentsJson(appIconSet, images)
    }

    private fun writeContentsJson(assetDir: File, images: List<Map<String, Any>>) =
        assetDir.resolve("Contents.json").writeText(
            groovy.json.JsonOutput.prettyPrint(
                groovy.json.JsonOutput.toJson(
                    mapOf(
                        "images" to images,
                        "info" to mapOf(
                            "version" to 1,
                            "author" to "xcode",
                        ),
                    ),
                ),
            ),
        )

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
