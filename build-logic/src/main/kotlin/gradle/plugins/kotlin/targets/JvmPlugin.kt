package gradle.plugins.kotlin.targets

import gradle.api.configureEach
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectProperties
import org.gradle.api.NamedDomainObjectCollection
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
                registerJvmStressTest()
                registerJavaCodegenTestTask()
            }
        }
    }

    private fun Project.registerJvmStressTest() {
        val jvmTest = tasks.withType<KotlinJvmTest>()
        if (jvmTest.isEmpty()) return

        val stress = tasks.register<Test>("stressTest") {
            classpath = files(jvmTest.map { it.classpath })
            testClassesDirs = files(jvmTest.map { it.testClassesDirs })
            maxHeapSize = "2g"
            jvmArgs("-XX:+HeapDumpOnOutOfMemoryError")
            forkEvery = 1
            systemProperty("enable.stress.tests", "true")
            include("**/*StressTest*")
            useJUnitPlatform()
        }

        jvmTest.configureEach { exclude("**/*StressTest*") }

        tasks.matching { task -> task.name == "check" }.configureEach { dependsOn(stress) }
    }

    /**
     * Registers a new testRun that substitutes the Kotlin models by the Java models.
     * Because they have the same JVM API, this is transparent to all tests that are in commonTest that work the same for Kotlin
     * & Java models.
     *
     * - For Java models, we create a separate compilation (and therefore sourceSet graph). The generated models are wired to the separate
     * commonJavaCodegenTest source set. Then the contents of commonTest/kotlin is sourced directly
     *
     * This breaks IDE support because now commonTest/kotlin is used from 2 different places so clicking a model there, it's impossible
     * to tell which model it is. We could expect/actual all of the model APIs but that'd be a lot of very manual work
     */
    private fun Project.registerJavaCodegenTestTask() {
        /**
         * This is an intermediate source set to make sure that we do not have expect/actual
         *in the same Kotlin module
         */
        kotlin.targets.withType<KotlinJvmTarget>()
            .takeIf(NamedDomainObjectCollection<*>::isNotEmpty)?.let { jvmTargets ->
                val commonTest = kotlin.sourceSets.getByName("commonTest")

                val commonJavaCodegenTest = kotlin.sourceSets.create("commonJavaCodegenTest") {
                    kotlin.srcDir(commonTest.kotlin.srcDirs.first())
                }

                jvmTargets.configureEach { jvmTarget ->
                    val baseTestCompilation = jvmTarget.compilations.getByName("test")
                    val testCompileClasspath = configurations.getByName(baseTestCompilation.compileDependencyConfigurationName)
                    val testRuntimeClasspath = configurations.getByName(baseTestCompilation.runtimeDependencyConfigurationName)

                    val javaCodegenCompilation = jvmTarget.compilations.create("javaCodegenTest").apply {
                        defaultSourceSet.dependsOn(commonJavaCodegenTest)

                        configurations.compileDependencyConfiguration.extendsFrom(testCompileClasspath)
                        configurations.runtimeDependencyConfiguration?.extendsFrom(testRuntimeClasspath)

                        compileJavaTaskProvider?.configure { classpath += testCompileClasspath }
                    }

                    val testRun = jvmTarget.testRuns.create("javaCodegen").apply {
                        setExecutionSourceFrom(javaCodegenCompilation)
                        executionTask.configure { useJUnitPlatform() }
                    }

                    tasks.matching { task -> task.name == "check" }.configureEach {
                        dependsOn(testRun.executionTask)
                    }
                }
            }
    }
}
