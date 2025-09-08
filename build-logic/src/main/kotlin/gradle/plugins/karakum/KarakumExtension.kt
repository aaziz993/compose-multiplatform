package gradle.plugins.karakum

import gradle.api.configureEach
import gradle.api.project.kotlin
import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import java.io.File
import klib.data.type.collections.map.asMap
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

context(project: Project)
public fun KarakumExtension.sourceSets(
    vararg sourceSetNames: String = project.kotlin.targets
        .withType<KotlinJsTargetDsl>()
        .filter { this::class == KotlinJsTargetDsl::class }
        .flatMap { target ->
            listOf("Main", "Test").map { compilationName -> "${target.name}$compilationName" }
        }.toTypedArray()
): Unit = project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
    project.file("karakum.config.json")
        .takeIf(File::exists)
        ?.let(File::readText)
        ?.let(Json.Default::decodeAnyFromString)
        ?.asMap
        ?.let { karakumConfig ->
            project.kotlin.sourceSets
                .matching { sourceSet -> sourceSet.name in sourceSetNames }
                .configureEach { sourceSet ->
                    sourceSet.kotlin.srcDir(project.file(karakumConfig["output"]!!))
                }
        }
}
