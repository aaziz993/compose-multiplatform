package gradle.plugins.karakum

import klib.data.type.collections.map.asMap
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

context(project: Project)
public fun KotlinSourceSet.setKarakumSrcDir(): Unit =
    project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
        project.file("karakum.config.json")
            .takeIf(File::exists)
            ?.let(File::readText)
            ?.let(Json.Default::decodeAnyFromString)
            ?.asMap
            ?.let { karakumConfig ->
                kotlin.srcDir(project.file(karakumConfig["output"]!!))
            }
    }