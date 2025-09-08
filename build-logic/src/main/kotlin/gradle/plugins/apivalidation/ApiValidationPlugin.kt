package gradle.plugins.apivalidation

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.api.tasks.apiBuild
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.named

public class ApiValidationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlinx.binary-compatibility-validator") {
                adjustApiBuildTask()
            }
        }
    }

    private fun Project.adjustApiBuildTask() {
        project.tasks.apiBuild {
            // "jar" here is the name of the default Jar task producing the resulting jar file
            // in a multiplatform project it can be named "jvmJar"
            // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
            inputJar.value(
                when {
                    pluginManager.hasPlugin("com.gradleup.shadow") -> tasks.named<ShadowJar>("shadowJar")
                    pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform") -> tasks.named<Jar>("jvmJar")
                    else -> project.tasks.named<Jar>("jar")
                }.flatMap { it.archiveFile },
            )
        }
    }
}
