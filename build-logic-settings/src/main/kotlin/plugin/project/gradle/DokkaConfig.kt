package plugin.project.gradle

import gradle.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.apply
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

internal fun Project.configureDokka() {
    apply(plugin = libs.plugins.dokka.get().pluginId)

    if(this==rootProject){
        configureRootProjectDokka()
    }else{
        configureSubProjectDokka()
    }

    val dokkaPlugin by configurations

    dependencies {
        dokkaPlugin(libs.dokka.versioning)
    }
}

private fun Project.configureRootProjectDokka(){
    extensions.configure<DokkaExtension> {
        dokkaSourceSets.configureEach {
            sourceLink {
                // Read docs for more details: https://kotlinlang.org/docs/dokka-gradle.html#source-link-configuration
                remoteUrl("https://github.com//dokka/tree/master/examples/gradle/dokka-multimodule-example")
                localDirectory.set(rootDir)
            }
        }
    }

    tasks.withType<DokkaMultiModuleTask>(::configureDokkaMultiModuleTask)
}

private fun Project.configureDokkaMultiModuleTask(task: DokkaMultiModuleTask) = task.apply {
    val version = project.version
    val dokkaOutputDir = "../versions"
    val id = "org.jetbrains.dokka.versioning.VersioningPlugin"
    val config = """{ "version": "$version", "olderVersionsDir":"$dokkaOutputDir" }"""

    outputDirectory = project.layout.projectDirectory.dir("$dokkaOutputDir/$version")
    pluginsMapConfiguration = mapOf(id to config)
}

private fun Project.configureSubProjectDokka(){
    extensions.configure<DokkaExtension> {
        dokkaSourceSets.configureEach {
            includes.from("Module.md")
        }
    }
}
