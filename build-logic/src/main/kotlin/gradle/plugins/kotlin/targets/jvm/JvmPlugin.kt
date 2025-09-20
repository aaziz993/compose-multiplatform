package gradle.plugins.kotlin.targets.jvm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

internal class JvmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
//                registerJvmStressTest()
            }
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
