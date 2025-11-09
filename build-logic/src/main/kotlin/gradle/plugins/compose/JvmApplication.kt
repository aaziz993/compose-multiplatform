package gradle.plugins.compose

import gradle.api.compose.compose
import gradle.api.compose.desktop
import gradle.api.project.sourceSets
import java.io.File
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.compose.desktop.application.dsl.JvmApplication
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.named
import proguard.gradle.ProGuardTask

context(project: Project)
public fun JvmApplication.configureProguard() {
    // Get the main Jar task safely
    val jarTasks = project.tasks.named<Jar>("jar")

    val allJars =
        jarTasks.get().outputs.files + project.sourceSets.named("main").get().runtimeClasspath.filter { file ->
            file.path.endsWith(".jar")
        }
            // workaround https://github.com/JetBrains/compose-jb/issues/1971
            .filterNot { file ->
                file.name.startsWith("skiko-awt-") && !file.name.startsWith("skiko-awt-runtime-")
            }
            .distinctBy(File::getName) // Prevent duplicate jars

    // Split the Jars to get the ones that need obfuscation and those that do not
    val (obfuscateJars, otherJars) = allJars.partition { file ->
        !file.name.contains("slf4j", ignoreCase = true)
            .or(file.name.contains("logback", ignoreCase = true))
    }

    // Proguard Task definition!
    val proguard by project.tasks.register<ProGuardTask>("proguard") {
        dependsOn(jarTasks.get())
        println("Config ProGuard")
        for (file in obfuscateJars) {
            injars(file)
            outjars(project.mapObfuscatedJarFile(file))
        }
        val library = if (System.getProperty("java.version").startsWith("1.")) "lib/rt.jar" else "jmods"
        libraryjars("${project.compose.desktop.application.javaHome.ifEmpty { System.getProperty("java.home") }}/$library")
        libraryjars(otherJars)
        configuration("desktop-proguard-rules.pro")
    }

    // Disable Compose Desktop default config and add your own Jars
    disableDefaultConfiguration()
    fromFiles(proguard.outputs.files.asFileTree)
    fromFiles(otherJars)
    mainJar.set(jarTasks.map { task -> RegularFile { project.mapObfuscatedJarFile(task.archiveFile.get().asFile) } })
}

// Map Files to a known path
private fun Project.mapObfuscatedJarFile(file: File) =
    File("${layout.buildDirectory}/tmp/obfuscated/${file.nameWithoutExtension}.min.jar")
