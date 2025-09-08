package gradle.plugins.karakum

import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import klib.data.type.collections.map.asMap
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun KarakumExtension.sourceSets(vararg sourceSets: KotlinSourceSet): Unit =
    project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
        project.file("karakum.config.json")
            .takeIf(File::exists)
            ?.let(File::readText)
            ?.let(Json.Default::decodeAnyFromString)
            ?.asMap
            ?.let { karakumConfig ->
                sourceSets.forEach { sourceSet ->
                    sourceSet.kotlin.srcDir(project.file(karakumConfig["output"]!!))
                }
            }
    }
