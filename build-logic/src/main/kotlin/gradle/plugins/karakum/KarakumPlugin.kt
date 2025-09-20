package gradle.plugins.karakum

import gradle.api.configureEach
import gradle.api.project.ProjectLayout
import gradle.api.project.dokka
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import java.io.File
import klib.data.type.collections.map.asMap
import klib.data.type.primitives.string.uppercaseFirstChar
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

public class KarakumPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
                adjustSourceSets()
            }
        }
    }

    private fun Project.adjustSourceSets(): Unit =
        project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
            project.file("karakum.config.json")
                .takeIf(File::exists)
                ?.let(File::readText)
                ?.let(Json.Default::decodeAnyFromString)
                ?.asMap
                ?.let { karakumConfig ->
                    val jsSourceSetNames = project.kotlin.targets
                        .filter { this::class == KotlinJsTargetDsl::class }
                        .flatMap { target ->
                            target.compilations.map { compilation -> "${target.name}${compilation.name.uppercaseFirstChar()}" }
                        }

                    project.kotlin.sourceSets
                        .matching { sourceSet -> sourceSet.name in jsSourceSetNames }
                        .configureEach { sourceSet ->
                            sourceSet.kotlin.srcDir(project.file(karakumConfig["output"]!!))
                        }
                }
        }
}
