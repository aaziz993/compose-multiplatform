@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose

import gradle.api.all
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.sourceSetsToComposeResourcesDirs
import gradle.plugins.compose.apple.Contents
import gradle.plugins.compose.apple.image.Image
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import javax.imageio.ImageIO
import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asMap
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.json.decodeFromAny
import kotlinx.serialization.json.Json
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

private val THEMES = listOf("", "-dark", "-light")
private const val APP_ICON = "app-icon"
private val DENSITIES = mapOf(
    "ldpi" to 18,
    "mdpi" to 24,
    "hdpi" to 36,
    "xhdpi" to 48,
    "xxhdpi" to 72,
    "xxxhdpi" to 96,
)
private val IOS_IMAGE_APPEARANCE = mapOf("" to 0, "dark" to 1, "tinted" to 2)
private const val IOS_APPICONSET_DIR = "appleApp/iosApp/Assets.xcassets/AppIcon.appiconset"
private const val TVOS_BRANDASSETS_DIR = "appleApp/TVosApp/Assets.xcassets/App Icon & Top Shelf Image.brandassets"
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

            val svg = composeResourcesDir.resolve("drawable$theme/$APP_ICON.svg").takeIf(File::exists)
                ?: return@forEachIndexed

            DENSITIES.entries.forEachIndexed { index, (qualifier, size) ->
                val drawableQualifiedDir = composeResourcesDir.resolve("drawable-$qualifier$theme")
                drawableQualifiedDir.mkdirs()

                val pngFile = drawableQualifiedDir.resolve("$APP_ICON-png.png")
                val icoFile = drawableQualifiedDir.resolve("$APP_ICON-ico.ico")

                svgToPng(svg, pngFile, size)
                Imaging.writeImage(ImageIO.read(pngFile), icoFile, ImageFormats.ICO)
            }

            val icnsFile = composeResourcesDir.resolve("drawable$theme/$APP_ICON-icns.icns")

            val icnsTempDir = Files.createTempDirectory("icns$theme-iconset").toFile()
            val pngFiles = MACOS_ICON_SIZES.map { size ->
                icnsTempDir.resolve("icon_${size}x${size}.png").apply {
                    svgToPng(svg, this, size)
                }
            }

            pngsToIconSet(pngFiles, icnsFile)

            icnsTempDir.deleteRecursively()
        }

        val drawableQualifiedDir = THEMES.firstNotNullOfOrNull { theme ->
            composeResourcesDir.resolve("drawable-${DENSITIES.keys.last()}$theme").takeIf(File::exists)
        }

        val icnsFile = THEMES.firstNotNullOfOrNull { theme ->
            composeResourcesDir.resolve("drawable$theme/$APP_ICON-icns.icns").takeIf(File::exists)
        }

        // jpackage only supports .png on Linux, .ico on Windows, .icns on Mac, so a developer must do a conversion (probably from a png) to a 3 different formats.
        // Also it seems that ico and icns need to contain an icon in multiple resolutions, so the conversion becomes a bit inconvenient.
        compose.desktop.application.nativeDistributions {
            linux {
                if (!iconFile.isPresent)
                    drawableQualifiedDir?.resolve("$APP_ICON-png.png")?.takeIf(File::exists)?.let { iconFile ->
                        this.iconFile = iconFile
                    }
            }
            windows {
                if (!iconFile.isPresent)
                    drawableQualifiedDir?.resolve("$APP_ICON-ico.ico")?.takeIf(File::exists)?.let { iconFile ->
                        this.iconFile = iconFile
                    }
            }
            macOS {
                if (!iconFile.isPresent && icnsFile != null) iconFile = icnsFile
            }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun Project.adjustAppleIcons(composeResourcesDir: File) {
        adjustIconSet(composeResourcesDir, rootProject.file(IOS_APPICONSET_DIR))
        adjustTVosIcons(composeResourcesDir, rootProject.file(TVOS_BRANDASSETS_DIR))
        adjustIconSet(composeResourcesDir, rootProject.file(WATCHOS_APPICONSET_DIR))
    }

    private fun adjustIconSet(composeResourcesDir: File, iconSetDir: File, forceOpaque: Boolean = false, transform: (Map<String, Any>) -> Map<String, Any> = { it }) {
        if (!iconSetDir.exists()) return

        val contents = iconSetDir.resolve("Contents.json")
            .takeIf(File::exists)?.readText()?.let(json::decodeAnyFromString)?.asMap ?: return

        val images: List<Image> = contents["images"]?.asList<Map<String, Any>>()?.map(transform)?.map(json::decodeFromAny)
            ?: return

        images.forEach { image ->
            image.appearances.filter { (appearance, _) -> appearance == "luminosity" }.forEach { (_, value) ->
                val themeIndex = IOS_IMAGE_APPEARANCE[value]!!
                val theme = THEMES[themeIndex]

                val svg = composeResourcesDir.resolve("drawable$theme/$APP_ICON.svg").takeIf(File::exists)
                    ?: return@forEach

                val (width, height) = image.size!!.split("x").map(String::toInt)
                val scale = image.scale?.removeSuffix("x")?.toInt() ?: 1

                val iconFile = iconSetDir.resolve(
                    image.filename
                        ?: "app-icon-${if (width == height) width else image.size}${if (scale == 1) "" else "@${scale}x"}${if (themeIndex == 0) "" else " $themeIndex"}.png",
                )

                svgToPng(svg, iconFile, width * scale, height * scale, forceOpaque)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun adjustTVosIcons(composeResourcesDir: File, brandAssetsDir: File) {
        if (!brandAssetsDir.exists()) return

        val brandAssetsContents = brandAssetsDir.resolve("Contents.json")
            .takeIf(File::exists)?.readText()?.let(json::decodeAnyFromString)?.asMap ?: return

        brandAssetsContents["assets"]?.asList?.forEach { asset ->
            asset as Map<String, Any>

            val brandAssetDir = brandAssetsDir.resolve(asset["filename"] as String).takeIf(File::exists)
                ?: return@forEach

            adjustIconSet(composeResourcesDir, brandAssetDir, true) { image ->
                buildMap {
                    asset["size"]?.let { size -> put("size", size) }
                    asset["scale"]?.let { scale -> put("scale", scale) }
                } + image
            }

            val brandAssetContents: Contents = brandAssetDir.resolve("Contents.json")
                .takeIf(File::exists)?.readText()?.let(json::decodeFromString) ?: Contents()

            brandAssetContents.layers?.forEach { layer ->
                val layerDir = brandAssetDir.resolve(layer.filename)
                adjustIconSet(composeResourcesDir, layerDir.resolve("Content.imageset"), true) { image ->
                    buildMap {
                        asset["size"]?.let { size -> put("size", size) }
                        asset["scale"]?.let { scale -> put("scale", scale) }
                    } + image
                }
            }
        }
    }

    private fun svgToPng(svgFile: File, pngFile: File, width: Int, height: Int, forceOpaque: Boolean = false) {
        FileInputStream(svgFile).use { input ->
            val transcoder = PNGTranscoder().apply {
                addTranscodingHint(PNGTranscoder.KEY_WIDTH, width.toFloat())
                addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height.toFloat())
                if (forceOpaque)
                    addTranscodingHint(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE, java.lang.Boolean.TRUE)
            }
            val transcoderInput = TranscoderInput(input)
            FileOutputStream(pngFile).use { output ->
                transcoder.transcode(transcoderInput, TranscoderOutput(output))
            }
        }

        if (forceOpaque) ensureOpaque(pngFile)
    }

    private fun svgToPng(svgFile: File, pngFile: File, size: Int, forceOpaque: Boolean = false) =
        svgToPng(svgFile, pngFile, size, size, forceOpaque)

    private fun ensureOpaque(pngFile: File) {
        val image = ImageIO.read(pngFile)
        val opaque = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        val graphics = opaque.createGraphics()
        graphics.drawImage(image, 0, 0, Color.WHITE, null) // fills transparent areas with white
        graphics.dispose()
        ImageIO.write(opaque, "png", pngFile)
    }

    private fun pngsToIconSet(pngFiles: List<File>, icnsFile: File) =
        Imaging.writeImage(ImageIO.read(pngFiles[3]), icnsFile, ImageFormats.ICNS)

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
