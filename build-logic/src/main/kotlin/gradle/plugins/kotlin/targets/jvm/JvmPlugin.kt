package gradle.plugins.kotlin.targets.jvm

import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.java
import gradle.api.project.projectScript
import klib.data.type.pair
import klib.data.type.primitives.string.lowercaseFirstChar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

internal class JvmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            adjustSourceSets()
            registerJvmStressTest()
        }
    }

    private fun Project.adjustSourceSets() = pluginManager.withPlugin("org.gradle.java-base") {
        when (val layout = project.projectScript.layout) {
            is ProjectLayout.Flat -> java.sourceSets.configureEach { sourceSet ->
                val compilationName = sourceSet.name.removePrefix("jvm").lowercaseFirstChar()

                val (srcPart, resourcesPart) =
                    if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) "src" to ""
                    else compilationName.pair()

                val targetPart = "${layout.targetDelimiter}java"

                sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPart$targetPart")
                sourceSet.resources.replace("src/${sourceSet.name}/resources", "${resourcesPart}Resources$targetPart".lowercaseFirstChar())
            }

            else -> Unit
        }
    }

    private fun Project.registerJvmStressTest() {
        val jvmTests = tasks.withType<KotlinJvmTest>()
        if (jvmTests.isEmpty()) return

        val stress = tasks.register<Test>("stressTest") {
            classpath = files(jvmTests.map { jvmTest -> jvmTest.classpath })
            testClassesDirs = files(jvmTests.map { jvmTest -> jvmTest.testClassesDirs })
            maxHeapSize = "2g"
            jvmArgs("-XX:+HeapDumpOnOutOfMemoryError")
            forkEvery = 1
            systemProperty("enable.stress.tests", "true")
            include("**/*StressTest*")
            useJUnitPlatform()
        }

        jvmTests.configureEach { exclude("**/*StressTest*") }

        tasks.matching { task -> task.name == "check" }.configureEach { dependsOn(stress) }
    }
}
